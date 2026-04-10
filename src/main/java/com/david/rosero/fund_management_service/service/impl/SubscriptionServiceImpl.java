package com.david.rosero.fund_management_service.service.impl;

import com.david.rosero.fund_management_service.dto.SubscriptionRequest;
import com.david.rosero.fund_management_service.dto.SubscriptionResponse;
import com.david.rosero.fund_management_service.dto.TransactionResponse;
import com.david.rosero.fund_management_service.exception.BusinessException;
import com.david.rosero.fund_management_service.exception.NotFoundException;
import com.david.rosero.fund_management_service.model.*;
import com.david.rosero.fund_management_service.repository.ClientRepository;
import com.david.rosero.fund_management_service.repository.SubscriptionRepository;
import com.david.rosero.fund_management_service.repository.TransactionRepository;
import com.david.rosero.fund_management_service.service.FundService;
import com.david.rosero.fund_management_service.service.NotificationService;
import com.david.rosero.fund_management_service.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final FundService fundService;
    private final ClientRepository clientRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    @Override
    public SubscriptionResponse subscribe(SubscriptionRequest request) {
        Fund fund = fundService.getByCode(request.getFundCode());
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new NotFoundException("Client not found: " + request.getClientId()));

        BigDecimal amount = BigDecimal.valueOf(request.getAmount());
        validateBalance(client, fund, amount);

        BigDecimal currentBalance = client.getBalance() != null ? client.getBalance() : BigDecimal.ZERO;
        client.setBalance(currentBalance.subtract(amount));
        clientRepository.save(client);

        Subscription subscription = Subscription.builder()
                .clientId(client.getId())
                .fundCode(fund.getCode())
                .amount(amount)
                .status(SubscriptionStatus.ACTIVE)
                .createdAt(Instant.now())
                .build();
        subscriptionRepository.save(subscription);

        Transaction tx = Transaction.builder()
                .type(TransactionType.SUBSCRIBE)
                .status(TransactionStatus.SUCCESS)
                .clientId(client.getId())
                .fundCode(fund.getCode())
                .amount(amount)
                .balanceAfter(client.getBalance())
                .message("Subscription created")
                .createdAt(Instant.now())
                .build();
        transactionRepository.save(tx);

        NotificationChannel channel = request.getNotificationChannel() != null
                ? request.getNotificationChannel()
                : client.getPreferredChannel();
        if (channel != null) {
            String destination = channel == NotificationChannel.EMAIL ? client.getEmail() : client.getPhone();
            notificationService.notify(destination, channel, "Subscribed to fund " + fund.getCode());
        }

        return SubscriptionResponse.builder()
                .subscriptionId(subscription.getId())
                .status(subscription.getStatus())
                .amount(subscription.getAmount())
                .fundCode(subscription.getFundCode())
                .createdAt(subscription.getCreatedAt())
                .clientBalance(client.getBalance())
                .message("Subscription successful")
                .build();
    }

    @Override
    public SubscriptionResponse cancel(String subscriptionId, String clientId) {
        Subscription subscription = subscriptionRepository.findByIdAndClientId(subscriptionId, clientId)
                .orElseThrow(() -> new NotFoundException("Subscription not found: " + subscriptionId));
        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new BusinessException("Subscription already cancelled");
        }

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Client not found: " + clientId));

        BigDecimal currentBalance = client.getBalance() != null ? client.getBalance() : BigDecimal.ZERO;
        client.setBalance(currentBalance.add(subscription.getAmount()));
        clientRepository.save(client);

        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancelledAt(Instant.now());
        subscriptionRepository.save(subscription);

        Transaction tx = Transaction.builder()
                .type(TransactionType.CANCEL)
                .status(TransactionStatus.SUCCESS)
                .clientId(client.getId())
                .fundCode(subscription.getFundCode())
                .amount(subscription.getAmount())
                .balanceAfter(client.getBalance())
                .message("Subscription cancelled")
                .createdAt(Instant.now())
                .build();
        transactionRepository.save(tx);

        return SubscriptionResponse.builder()
                .subscriptionId(subscription.getId())
                .status(subscription.getStatus())
                .amount(subscription.getAmount())
                .fundCode(subscription.getFundCode())
                .createdAt(subscription.getCreatedAt())
                .clientBalance(client.getBalance())
                .message("Cancellation successful")
                .build();
    }

    @Override
    public List<TransactionResponse> history(String clientId) {
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

    private void validateBalance(Client client, Fund fund, BigDecimal amount) {
        BigDecimal minAmount = fund.getMinimumAmount() != null ? fund.getMinimumAmount() : BigDecimal.ZERO;
        if (amount.compareTo(minAmount) < 0) {
            throw new BusinessException("Amount below minimum for fund " + fund.getCode());
        }
        BigDecimal balance = client.getBalance() != null ? client.getBalance() : BigDecimal.ZERO;
        if (balance.compareTo(amount) < 0) {
            throw new BusinessException("No tiene saldo disponible para vincularse al fondo " + fund.getCode());
        }
    }
}
