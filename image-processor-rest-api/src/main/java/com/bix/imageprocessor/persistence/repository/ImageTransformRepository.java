package com.bix.imageprocessor.persistence.repository;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTransformRepository extends JpaRepository<ImageTransform, Long> {
}