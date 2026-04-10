package com.david.rosero.fund_management_service.dto;

import com.david.rosero.fund_management_service.model.SubscriptionStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class SubscriptionResponse {
    String subscriptionId;
    SubscriptionStatus status;
    BigDecimal amount;
    String fundCode;
    Instant createdAt;
    BigDecimal clientBalance;
    String message;
}
