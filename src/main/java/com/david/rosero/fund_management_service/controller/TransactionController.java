package com.david.rosero.fund_management_service.controller;

import com.david.rosero.fund_management_service.dto.TransactionResponse;
import com.david.rosero.fund_management_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionResponse> byClient(@RequestParam String clientId) {
        return transactionService.findByClientId(clientId);
    }
}
