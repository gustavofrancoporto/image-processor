package com.bix.imageprocessor.persistence.impl;

import com.bix.imageprocessor.domain.notification.model.Notification;
import com.bix.imageprocessor.persistence.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Notification findByImageTransformId(Long id) {

        var query = """
                select
                    image_transformations.id as image_transformations_id,
                    image_transformations.status as image_transformations_status,
                    images.file_name as image_file_name,
                    users.email as email
                from image_transformations
                    left join images on images.id = image_transformations.image_id
                    left join users on image_transformations.requested_by_id = users.id
                where
                    image_transformations.id = :id
                """;

        var rowMapper = new DataClassRowMapper<>(Notification.class);

        return jdbcTemplate.queryForObject(query, Map.of("id", id), rowMapper);
    }
}
