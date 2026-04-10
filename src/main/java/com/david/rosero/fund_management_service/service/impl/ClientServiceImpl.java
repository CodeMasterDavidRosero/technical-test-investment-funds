package com.david.rosero.fund_management_service.service.impl;

import com.david.rosero.fund_management_service.exception.NotFoundException;
import com.david.rosero.fund_management_service.model.Client;
import com.david.rosero.fund_management_service.repository.ClientRepository;
import com.david.rosero.fund_management_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Client get(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found: " + id));
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
