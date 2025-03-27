package com.bix.imageprocessor.domain.imagetransformers;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;

public interface ImageTransformer {

    byte[] transform(byte[] image, ImageTransformParams params);

    boolean apply(ImageTransformParams params);
}
