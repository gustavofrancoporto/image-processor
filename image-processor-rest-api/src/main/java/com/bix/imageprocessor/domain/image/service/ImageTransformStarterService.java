package com.bix.imageprocessor.domain.image.service;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;

public interface ImageTransformStarterService {
    void startProcess(ImageDto image, ImageTransformParamsDto transformation, User user);
}
