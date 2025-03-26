package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.image.service.*;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.persistence.repository.ImageTransformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.bix.imageprocessor.domain.image.model.ImageTransformStatus.PENDING;

@Service
@RequiredArgsConstructor
public class ImageTransformServiceImpl implements ImageTransformService {

    private final ImageTransformRepository imageTransformIdRepository;

    @Override
    public ImageTransform createImageTransform(Image image, ImageTransformParams transformationParams, User user) {

        var imageTransformId = new ImageTransform();
        imageTransformId.setImage(image);
        imageTransformId.setTransformationParams(transformationParams);
        imageTransformId.setRequestedBy(user);
        imageTransformId.setRequestedAt(Instant.now());
        imageTransformId.setStatus(PENDING);

        return imageTransformIdRepository.save(imageTransformId);
    }
}
