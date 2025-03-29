package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.image.service.ImageTransformService;
import com.bix.imageprocessor.domain.imagetransformers.ImageTransformer;
import com.bix.imageprocessor.domain.notification.messaging.NotificationProducer;
import com.bix.imageprocessor.persistence.ImageTransformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageTransformServiceImpl implements ImageTransformService {

    private final ImageTransformRepository imageTransformRepository;
    private final NotificationProducer notificationProducer;
    private final List<ImageTransformer<? extends ImageTransformParams>> imageTransformers;

    @Override
    @Transactional
    public void process(Long id) {

        var imageTransform = imageTransformRepository.findById(id);
        log.info("Processing image transform {}", id);

        try {
            var transformedImage = transform(imageTransform);
            handleSuccess(imageTransform, transformedImage);
        } catch (Exception e) {
            handleError(imageTransform, e);
        }
    }

    private byte[] transform(ImageTransform imageTransform) {

        byte[] transformedImage = imageTransform.image();
        for (var imageTransformer : imageTransformers) {
            if (imageTransformer.apply(imageTransform.asParams())) {
                transformedImage = imageTransformer.transform(transformedImage, imageTransform.asParams());
            }
        }
        return transformedImage;
    }

    private void handleSuccess(ImageTransform imageTransform, byte[] transformedImage) {
        log.info("Image transform {} processed successfully", imageTransform.id());
        imageTransformRepository.markTransformationSuccess(imageTransform.id(), transformedImage);
        notificationProducer.notify(imageTransform, true);
    }

    private void handleError(ImageTransform imageTransform, Exception ex) {
        log.error("Image transform {} failed", imageTransform.id(), ex);
        imageTransformRepository.markTransformationFailed(imageTransform.id());
        notificationProducer.notify(imageTransform, false);
    }
}
