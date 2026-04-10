package com.david.rosero.fund_management_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscriptions")
public class Subscription {
    @Id
    private String id;
    private String clientId;
    private String fundCode;
    private BigDecimal amount;
    private SubscriptionStatus status;

    @CreatedDate
    private Instant createdAt;
    private Instant cancelledAt;
}
