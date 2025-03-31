package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.persistence.repository.ImageTransformRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static com.bix.imageprocessor.domain.image.model.ImageTransformStatus.PENDING;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageTransformServiceTest {

    @Mock ImageTransformRepository imageTransformRepository;

    @InjectMocks ImageTransformServiceImpl imageTransformService;

    @Mock User user;
    @Mock Image image;
    @Mock ImageTransformParams params;
    @Mock UUID uuid;
    @Mock Instant now;
    String uuidText = randomUUID().toString();

    @Captor ArgumentCaptor<ImageTransform> imageTransformCaptor;

    @Test
    void shouldSaveImageTransformSuccessfully() {


        try (
                var instantMockedStatic = mockStatic(Instant.class);
                var uuidMockedStatic = mockStatic(UUID.class)
        ) {

            instantMockedStatic.when(Instant::now).thenReturn(now);
            uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuid);

            when(uuid.toString()).thenReturn(uuidText);

            imageTransformService.createImageTransform(image, params, user);

            verify(imageTransformRepository, only()).save(imageTransformCaptor.capture());

            var imageTransform = imageTransformCaptor.getValue();

            assertThat(imageTransform.getImage()).isEqualTo(image);
            assertThat(imageTransform.getTransformationParams()).isEqualTo(params);
            assertThat(imageTransform.getRequestedBy()).isEqualTo(user);
            assertThat(imageTransform.getRequestedAt()).isEqualTo(now);
            assertThat(imageTransform.getDownloadCode()).isEqualTo(uuidText.replaceAll("-", ""));
            assertThat(imageTransform.getStatus()).isEqualTo(PENDING);
            assertThat(imageTransform.getCompletedAt()).isNull();
        }
    }
}
