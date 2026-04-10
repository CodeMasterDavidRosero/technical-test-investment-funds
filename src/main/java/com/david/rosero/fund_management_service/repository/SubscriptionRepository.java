package com.david.rosero.fund_management_service.repository;

import com.david.rosero.fund_management_service.model.Subscription;
import com.david.rosero.fund_management_service.model.SubscriptionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findByClientId(String clientId);
    Optional<Subscription> findByIdAndClientId(String id, String clientId);
    List<Subscription> findByClientIdAndStatus(String clientId, SubscriptionStatus status);
}
