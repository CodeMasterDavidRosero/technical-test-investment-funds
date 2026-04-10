package com.david.rosero.fund_management_service.service;

import com.david.rosero.fund_management_service.model.Client;

public interface ClientService {
    Client get(String id);
    Client save(Client client);
}
