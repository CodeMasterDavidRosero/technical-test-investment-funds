package com.david.rosero.fund_management_service.repository;

import com.david.rosero.fund_management_service.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByClientIdOrderByCreatedAtDesc(String clientId);
}
