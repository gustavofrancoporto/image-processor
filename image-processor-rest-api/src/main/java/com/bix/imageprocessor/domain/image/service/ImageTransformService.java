package com.bix.imageprocessor.domain.image.service;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.user.model.User;

public interface ImageTransformService {
    ImageTransform createImageTransform(Image image, ImageTransformParams transformationParams, User user);
}
