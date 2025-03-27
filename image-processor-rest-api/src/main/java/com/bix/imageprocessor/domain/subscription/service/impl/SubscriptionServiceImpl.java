package com.bix.imageprocessor.domain.subscription.service.impl;

import com.bix.imageprocessor.domain.subscription.service.SubscriptionService;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.persistence.repository.ImageTransformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final ImageTransformRepository imageTransformRepository;

    @Override
    public boolean hasUserReachedLimit(User user) {

        var dailyLimit = user.getSubscription().getMaxTransformationsPerDay();
        if (dailyLimit == null) {
            return false;
        }

        var totalRequestsToday = imageTransformRepository.getTotalRequestsToday(user.getId());

        return dailyLimit <= totalRequestsToday;
    }
}
