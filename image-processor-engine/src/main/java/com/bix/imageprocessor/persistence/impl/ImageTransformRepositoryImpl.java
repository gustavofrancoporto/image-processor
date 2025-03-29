package com.bix.imageprocessor.persistence.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformStatus;
import com.bix.imageprocessor.persistence.ImageTransformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.Instant;
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
                    image_transformations.id,
                    image_transformations.download_code as image_code,
                    images.data as image,
                    images.file_name as image_file_name,
                    users.email as requestor_email,
                    image_transformation_params.resize_ratio,
                    image_transformation_params.grayscale,
                    image_transformation_params.invert_colors,
                    image_transformation_params.sepia_intensity
                from image_transformations
                    left join image_transformation_params on image_transformation_params.id = image_transformations.transformation_params_id
                    left join images on images.id = image_transformations.image_id
                    left join users on image_transformations.requested_by_id = users.id
                where
                    image_transformations.id = :id
                """;

        var rowMapper = new DataClassRowMapper<>(ImageTransform.class);

        return jdbcTemplate.queryForObject(query, Map.of("id", id), rowMapper);
    }

    @Override
    public void markTransformationSuccess(Long id, byte[] image) {

        var query = """
                update image_transformations
                set
                    transformed_data = :image,
                    status = :status,
                    completed_at = :completedAt
                where id = :id""";

        var paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        paramSource.addValue("status", ImageTransformStatus.SUCCESS.name());
        paramSource.addValue("completedAt", Timestamp.from(Instant.now()));
        paramSource.addValue("image", new SqlLobValue(new ByteArrayInputStream(image), image.length), BLOB);

        jdbcTemplate.update(query, paramSource);
    }

    @Override
    public void markTransformationFailed(Long id) {

        var query = """
                update image_transformations
                set
                    transformed_data = null,
                    status = :status,
                    completed_at = :completedAt
                where id = :id""";

        var paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        paramSource.addValue("status", ImageTransformStatus.ERROR.name());
        paramSource.addValue("completedAt", Timestamp.from(Instant.now()));

        jdbcTemplate.update(query, paramSource);


    }
}
