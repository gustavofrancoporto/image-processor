package com.bix.imageprocessor.persistence.repository;

import com.bix.imageprocessor.domain.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}