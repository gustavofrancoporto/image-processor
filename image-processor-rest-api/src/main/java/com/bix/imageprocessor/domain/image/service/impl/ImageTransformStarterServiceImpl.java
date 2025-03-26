package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.service.*;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageTransformStarterServiceImpl implements ImageTransformStarterService {

    private final ImageService imageService;
    private final ImageTransformParamsService imageTransformIdParamsService;
    private final ImageTransformService imageTransformIdService;
    private final ImageTransformQueueSender imageProcessorQueueSender;

    @Override
    @Transactional
    public void startProcess(ImageDto imageDto, ImageTransformParamsDto imageTransformParamsDto, User user) {

        var image = imageService.save(imageDto);
        var transformationParams = imageTransformIdParamsService.save(imageTransformParamsDto);
        var imageTransformId = imageTransformIdService.createImageTransform(image, transformationParams, user);

        imageProcessorQueueSender.send(imageTransformId.getId());
    }
}
