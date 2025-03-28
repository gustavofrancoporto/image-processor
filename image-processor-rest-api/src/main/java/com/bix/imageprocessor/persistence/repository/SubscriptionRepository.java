package com.bix.imageprocessor.persistence.repository;

import com.bix.imageprocessor.domain.subscription.model.Subscription;
import com.bix.imageprocessor.domain.subscription.model.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByType(SubscriptionType type);
}