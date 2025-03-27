package com.bix.imageprocessor.persistence.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.persistence.ImageTransformRepository;
import com.bix.imageprocessor.persistence.ImageTransformRowMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlBinaryValue;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static java.sql.Types.BLOB;

@Repository
@RequiredArgsConstructor
public class ImageTransformRepositoryImpl implements ImageTransformRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public ImageTransform findById(Long id) {

        var query = """
                select
                    image_transforms.id,
                    images.original_data as image,
                    image_transform_params.resize_ratio,
                    image_transform_params.filters
                from image_transforms
                    left join image_transform_params on image_transform_params.id = image_transforms.transformation_params_id
                    left join images on images.id = image_transforms.image_id
                where image_transforms.id = :id
                """;

        var rowMapper = new ImageTransformRowMapperImpl();

        return jdbcTemplate.queryForObject(query, Map.of("id", id), rowMapper);
    }

    @Override
    public void updateProcessedImage(Long id, byte[] image) {

        var query = "update image_transforms set processed_data = :image where id = :id";

        var paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        paramSource.addValue("image", new SqlLobValue(new ByteArrayInputStream(image), image.length), BLOB);

        jdbcTemplate.update(query, paramSource);
    }
}
