package com.bix.imageprocessor.persistence.repository;

import com.bix.imageprocessor.domain.subscription.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}