package com.david.rosero.fund_management_service.controller;

import com.david.rosero.fund_management_service.model.Client;
import com.david.rosero.fund_management_service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepository;

    @GetMapping
    public List<Client> list() {
        return clientRepository.findAll();
    }
}
