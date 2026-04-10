package com.david.rosero.fund_management_service.repository;

import com.david.rosero.fund_management_service.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String> {
}
