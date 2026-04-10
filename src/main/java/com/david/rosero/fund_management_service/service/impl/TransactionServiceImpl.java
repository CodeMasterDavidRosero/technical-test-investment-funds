package com.david.rosero.fund_management_service.service.impl;

import com.david.rosero.fund_management_service.dto.TransactionResponse;
import com.david.rosero.fund_management_service.repository.TransactionRepository;
import com.david.rosero.fund_management_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<TransactionResponse> findByClientId(String clientId) {
        return transactionRepository.findByClientIdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(tx -> TransactionResponse.builder()
                        .id(tx.getId())
                        .type(tx.getType())
                        .status(tx.getStatus())
                        .fundCode(tx.getFundCode())
                        .amount(tx.getAmount())
                        .balanceAfter(tx.getBalanceAfter())
                        .message(tx.getMessage())
                        .createdAt(tx.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
