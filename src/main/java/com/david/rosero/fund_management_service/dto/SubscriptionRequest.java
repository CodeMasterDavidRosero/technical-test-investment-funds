package com.david.rosero.fund_management_service.dto;

import com.david.rosero.fund_management_service.model.NotificationChannel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {
    @NotBlank
    private String clientId;

    @NotBlank
    private String fundCode;

    @NotNull
    @Min(1)
    private Long amount;

    private NotificationChannel notificationChannel;
}
