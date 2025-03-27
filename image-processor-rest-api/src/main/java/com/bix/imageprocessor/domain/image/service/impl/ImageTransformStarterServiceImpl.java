package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.service.*;
import com.bix.imageprocessor.domain.subscription.service.SubscriptionService;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import com.bix.imageprocessor.web.exception.model.RequestLimitReachedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageTransformStarterServiceImpl implements ImageTransformStarterService {

    private final ImageService imageService;
    private final ImageTransformParamsService imageTransformParamsService;
    private final ImageTransformService imageTransformService;
    private final ImageTransformQueueSender imageTransformQueueSender;
    private final SubscriptionService subscriptionService;

    @Override
    @Transactional
    public void startProcess(ImageDto imageDto, ImageTransformParamsDto imageTransformParamsDto, User user) {

        if (subscriptionService.hasUserReachedLimit(user)) {
            throw new RequestLimitReachedException();
        }

        var image = imageService.save(imageDto);
        log.info("Image {} saved with id {}.", image.getFileName(), image.getId());

        var transformationParams = imageTransformParamsService.save(imageTransformParamsDto);
        log.info("Transformation params {} saved with id {}.", imageTransformParamsDto.toString(), transformationParams.getId());

        var imageTransform = imageTransformService.createImageTransform(image, transformationParams, user);
        log.info("Transformation created with id {}.", imageTransform.getId());

        imageTransformQueueSender.send(imageTransform.getId());
        log.info("Transformation {} sent to queue for processing.", imageTransform.getId());
    }
}
