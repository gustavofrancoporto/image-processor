package com.bix.imageprocessor.persistence;

import com.bix.imageprocessor.domain.image.model.ImageTransform;

public interface ImageTransformRepository {
    ImageTransform findById(Long id);

    void markTransformationSuccess(Long id, byte[] transformedImage);

    void markTransformationFailed(Long id);
}
