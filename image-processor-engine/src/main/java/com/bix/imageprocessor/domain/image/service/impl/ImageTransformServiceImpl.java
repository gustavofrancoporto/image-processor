package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.service.ImageTransformService;
import com.bix.imageprocessor.domain.imagetransformers.ImageTransformer;
import com.bix.imageprocessor.persistence.ImageTransformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageTransformServiceImpl implements ImageTransformService {

    private final ImageTransformRepository imageTransformRepository;
    private final List<ImageTransformer> imageTransformers;

    @Override
    @Transactional
    public void process(Long id) {

        var imageTransform = imageTransformRepository.findById(id);

        byte[] transformedImage = imageTransform.image();

        for (var imageTransformer : imageTransformers) {
            if (imageTransformer.apply(imageTransform.params())) {
                transformedImage = imageTransformer.transform(transformedImage, imageTransform.params());
            }
        }
        imageTransformRepository.updateProcessedImage(id, transformedImage);

    }
}
