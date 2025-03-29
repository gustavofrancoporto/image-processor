package com.bix.imageprocessor.persistence.repository;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageTransformRepository extends JpaRepository<ImageTransform, Long> {

    @Query(value = """
            select
                count(id)
            from image_transformations
            where
                date_trunc('day', requested_at) = date_trunc('day', timezone('utc', now())) and
                requested_by_id = ?
            """, nativeQuery = true)
    Integer getTotalRequestsToday(Long userId);

    Optional<ImageTransform> findByDownloadCode(String code);
}