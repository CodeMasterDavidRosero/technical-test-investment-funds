package com.david.rosero.fund_management_service.dto;

import com.david.rosero.fund_management_service.model.TransactionStatus;
import com.david.rosero.fund_management_service.model.TransactionType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class TransactionResponse {
    String id;
    TransactionType type;
    TransactionStatus status;
    String fundCode;
    BigDecimal amount;
    BigDecimal balanceAfter;
    String message;
    Instant createdAt;
}
