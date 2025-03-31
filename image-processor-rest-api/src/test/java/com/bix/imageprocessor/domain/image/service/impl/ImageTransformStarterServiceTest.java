package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.model.Image;
import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.image.model.ImageTransformParams;
import com.bix.imageprocessor.domain.image.service.ImageService;
import com.bix.imageprocessor.domain.image.service.ImageTransformParamsService;
import com.bix.imageprocessor.domain.image.service.ImageTransformQueueSender;
import com.bix.imageprocessor.domain.image.service.ImageTransformService;
import com.bix.imageprocessor.domain.subscription.service.SubscriptionService;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.utils.DateUtils;
import com.bix.imageprocessor.web.dto.image.ImageDto;
import com.bix.imageprocessor.web.dto.image.ImageTransformParamsDto;
import com.bix.imageprocessor.web.exception.model.RequestLimitReachedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageTransformStarterServiceTest {

    @Mock ImageService imageService;
    @Mock ImageTransformParamsService imageTransformParamsService;
    @Mock ImageTransformService imageTransformService;
    @Mock ImageTransformQueueSender imageTransformQueueSender;
    @Mock SubscriptionService subscriptionService;

    @InjectMocks ImageTransformStarterServiceImpl imageTransformStarterService;

    @Mock User user;
    @Mock Image image;
    @Mock ImageDto imageDto;
    @Mock ImageTransformParamsDto paramsDto;
    @Mock ImageTransformParams params;
    @Mock ImageTransform imageTransform;

    Long imageTransformId = 1L;


    @Test
    void shouldFailIfUserHasReachedDailyLimit() {

        var retryAfter = 1000L;

        try (var dateUtilsMockedStatic = mockStatic(DateUtils.class)) {

            dateUtilsMockedStatic.when(DateUtils::getSecondsUntilNextUtcDay).thenReturn(retryAfter);

            when(subscriptionService.hasUserReachedLimit(user)).thenReturn(true);

            assertThatExceptionOfType(RequestLimitReachedException.class)
                    .isThrownBy(() -> imageTransformStarterService.startProcess(imageDto, paramsDto, user))
                    .hasFieldOrPropertyWithValue("retryAfter", retryAfter);

        }
    }

    @Test
    void shouldStartImageTransformProcess() {

        when(subscriptionService.hasUserReachedLimit(user)).thenReturn(false);
        when(imageService.save(imageDto)).thenReturn(image);
        when(imageTransformParamsService.save(paramsDto)).thenReturn(params);
        when(imageTransformService.createImageTransform(image, params, user)).thenReturn(imageTransform);
        when(imageTransform.getId()).thenReturn(imageTransformId);

        imageTransformStarterService.startProcess(imageDto, paramsDto, user);

        verify(imageTransformQueueSender).send(imageTransformId);
    }
}
