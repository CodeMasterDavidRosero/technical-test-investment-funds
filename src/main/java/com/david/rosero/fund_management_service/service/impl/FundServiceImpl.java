package com.david.rosero.fund_management_service.service.impl;

import com.david.rosero.fund_management_service.exception.NotFoundException;
import com.david.rosero.fund_management_service.model.Fund;
import com.david.rosero.fund_management_service.repository.FundRepository;
import com.david.rosero.fund_management_service.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;

    @Override
    public List<Fund> listFunds() {
        return fundRepository.findAll();
    }

    @Override
    public Fund getByCode(String code) {
        return fundRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Fund not found: " + code));
    }
}
