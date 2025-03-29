package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.image.service.ImageTransformService;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.persistence.repository.ImageTransformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.bix.imageprocessor.domain.image.model.ImageTransformStatus.PENDING;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class ImageTransformServiceImpl implements ImageTransformService {

    private final ImageTransformRepository imageTransformRepository;

    @Override
    public ImageTransform createImageTransform(Image image, ImageTransformParams transformationParams, User user) {

        var imageTransformation = ImageTransform.builder()
                .image(image)
                .transformationParams(transformationParams)
                .requestedBy(user)
                .requestedAt(Instant.now())
                .status(PENDING)
                .downloadCode(randomUUID().toString().replaceAll("-", ""))
                .build();

        return imageTransformRepository.save(imageTransformation);
    }
}
