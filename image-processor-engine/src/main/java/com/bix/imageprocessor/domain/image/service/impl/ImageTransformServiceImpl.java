package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.image.service.ImageTransformService;
import com.bix.imageprocessor.domain.imagetransformers.ImageTransformer;
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
    private final List<ImageTransformer<? extends ImageTransformParams>> imageTransformers;

    @Override
    @Transactional
    public void process(Long id) {

        var imageTransform = imageTransformRepository.findById(id);
        log.info("Processing image transform {}", id);

        byte[] transformedImage = imageTransform.image();

        try {
            for (var imageTransformer : imageTransformers) {
                if (imageTransformer.apply(imageTransform.asParams())) {
                    transformedImage = imageTransformer.transform(transformedImage, imageTransform.asParams());
                }
            }
            log.info("Image transform {} processed successfully", id);
            imageTransformRepository.markTransformationSuccess(id, transformedImage);
        } catch (Exception e) {
            log.error("Image transform {} failed", id, e);
            imageTransformRepository.markTransformationFailed(id);
        }
    }
}
