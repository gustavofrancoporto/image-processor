package com.bix.imageprocessor.persistence.repository;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTransformParamsRepository extends JpaRepository<ImageTransformParams, Long> {
}