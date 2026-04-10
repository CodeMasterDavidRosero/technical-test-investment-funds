package com.david.rosero.fund_management_service.config;

import com.david.rosero.fund_management_service.model.Client;
import com.david.rosero.fund_management_service.model.Fund;
import com.david.rosero.fund_management_service.model.FundCategory;
import com.david.rosero.fund_management_service.model.NotificationChannel;
import com.david.rosero.fund_management_service.model.Transaction;
import com.david.rosero.fund_management_service.model.TransactionStatus;
import com.david.rosero.fund_management_service.model.TransactionType;
import com.david.rosero.fund_management_service.repository.ClientRepository;
import com.david.rosero.fund_management_service.repository.FundRepository;
import com.david.rosero.fund_management_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final FundRepository fundRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    @Value("${app.initial-balance:500000}")
    private BigDecimal initialBalance;

    @Bean
    CommandLineRunner seedData() {
        return args -> {
            List<Fund> funds = seedFunds();
            Client client = seedDefaultClient();
            if (client != null && funds != null && !funds.isEmpty()) {
                seedTransactions(client, funds);
            }
        };
    }

    private List<Fund> seedFunds() {
        if (fundRepository.count() > 0) {
            return fundRepository.findAll();
        }
        List<Fund> funds = List.of(
                Fund.builder().code("FPV_RECAUDADORA").name("Recaudadora").minimumAmount(BigDecimal.valueOf(75_000)).category(FundCategory.FPV).build(),
                Fund.builder().code("FPV_ECOPETROL").name("Ecopetrol").minimumAmount(BigDecimal.valueOf(125_000)).category(FundCategory.FPV).build(),
                Fund.builder().code("DEUDAPRIVADA").name("Deuda Privada").minimumAmount(BigDecimal.valueOf(50_000)).category(FundCategory.FIC).build(),
                Fund.builder().code("FDO_ACCIONES").name("Fondo Acciones").minimumAmount(BigDecimal.valueOf(250_000)).category(FundCategory.FIC).build(),
                Fund.builder().code("FPV_DINAMICA").name("Dinamica").minimumAmount(BigDecimal.valueOf(100_000)).category(FundCategory.FPV).build()
        );
        fundRepository.saveAll(funds);
        log.info("Seeded {} funds", funds.size());
        return funds;
    }

    private Client seedDefaultClient() {
        if (clientRepository.count() > 0) {
            return clientRepository.findAll().stream().findFirst().orElse(null);
        }
        Client client = Client.builder()
                .name("Test User")
                .email("user@example.com")
                .phone("+573001112233")
                .preferredChannel(NotificationChannel.EMAIL)
                .balance(initialBalance)
                .build();
        clientRepository.save(client);
        log.info("Seeded default client with balance {}", initialBalance);
        return client;
    }

    private void seedTransactions(Client client, List<Fund> funds) {
        if (transactionRepository.count() > 0) {
            return;
        }
        Fund fund = funds.get(0);
        BigDecimal amount = fund.getMinimumAmount();

        Transaction subscribeTx = Transaction.builder()
                .type(TransactionType.SUBSCRIBE)
                .status(TransactionStatus.SUCCESS)
                .clientId(client.getId())
                .fundCode(fund.getCode())
                .amount(amount)
                .balanceAfter(client.getBalance().subtract(amount))
                .message("Seed subscription to " + fund.getCode())
                .build();

        Transaction cancelTx = Transaction.builder()
                .type(TransactionType.CANCEL)
                .status(TransactionStatus.SUCCESS)
                .clientId(client.getId())
                .fundCode(fund.getCode())
                .amount(amount)
                .balanceAfter(client.getBalance())
                .message("Seed cancellation of " + fund.getCode())
                .build();

        transactionRepository.saveAll(List.of(subscribeTx, cancelTx));
        log.info("Seeded {} transactions for client {}", 2, client.getId());
    }
}
