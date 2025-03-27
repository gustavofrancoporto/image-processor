package com.bix.imageprocessor.persistence;

import com.bix.imageprocessor.domain.image.model.ImageTransform;

public interface ImageTransformRepository {
    ImageTransform findById(Long id);

    void updateProcessedImage(Long id, byte[] finalImage);
}
