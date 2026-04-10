package com.david.rosero.fund_management_service.controller;

import com.david.rosero.fund_management_service.dto.SubscriptionRequest;
import com.david.rosero.fund_management_service.dto.SubscriptionResponse;
import com.david.rosero.fund_management_service.dto.TransactionResponse;
import com.david.rosero.fund_management_service.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public SubscriptionResponse subscribe(@Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.subscribe(request);
    }

    @DeleteMapping("/{subscriptionId}")
    public SubscriptionResponse cancel(@PathVariable String subscriptionId,
                                       @RequestParam String clientId) {
        return subscriptionService.cancel(subscriptionId, clientId);
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> history(@RequestParam String clientId) {
        return subscriptionService.history(clientId);
    }
}
