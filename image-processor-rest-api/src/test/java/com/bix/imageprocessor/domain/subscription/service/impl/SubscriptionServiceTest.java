package com.bix.imageprocessor.domain.subscription.service.impl;

import com.bix.imageprocessor.domain.subscription.model.Subscription;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.persistence.repository.ImageTransformRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = LENIENT)
public class SubscriptionServiceTest {

    @Mock ImageTransformRepository imageTransformRepository;
    @InjectMocks SubscriptionServiceImpl subscriptionService;

    @Mock User user;
    @Mock Subscription subscription;

    Long userId = 1L;

    @ParameterizedTest
    @CsvSource({
            "  , 1 , false",
            "  , 11, false",
            "10, 1 , false",
            "10, 11, true"
    })
    void shouldReturnFalseIfSubscriptionDoesNotSpecifyALimit(Integer maxPerDay, Integer requestedToday, boolean expectedResult) {

        when(user.getId()).thenReturn(userId);
        when(user.getSubscription()).thenReturn(subscription);
        when(subscription.getMaxTransformationsPerDay()).thenReturn(maxPerDay);
        when(imageTransformRepository.getTotalRequestsToday(userId)).thenReturn(requestedToday);

        var result = subscriptionService.hasUserReachedLimit(user);

        assertThat(result).isEqualTo(expectedResult);
    }

}
