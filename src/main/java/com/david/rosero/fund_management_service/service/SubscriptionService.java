package com.david.rosero.fund_management_service.service;

import com.david.rosero.fund_management_service.dto.SubscriptionRequest;
import com.david.rosero.fund_management_service.dto.SubscriptionResponse;
import com.david.rosero.fund_management_service.dto.TransactionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse subscribe(SubscriptionRequest request);
    SubscriptionResponse cancel(String subscriptionId, String clientId);
    List<TransactionResponse> history(String clientId);
}
