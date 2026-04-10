package com.david.rosero.fund_management_service.repository;

import com.david.rosero.fund_management_service.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FundRepository extends MongoRepository<Fund, String> {
    Optional<Fund> findByCode(String code);
    boolean existsByCode(String code);
}
