package com.bix.imageprocessor.domain.subscription.service;

import com.bix.imageprocessor.domain.user.model.User;

public interface SubscriptionService {
    boolean hasUserReachedLimit(User user);
}
