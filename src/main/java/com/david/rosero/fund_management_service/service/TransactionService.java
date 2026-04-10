package com.david.rosero.fund_management_service.service;

import com.david.rosero.fund_management_service.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {
    List<TransactionResponse> findByClientId(String clientId);
}
