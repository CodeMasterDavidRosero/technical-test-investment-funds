package com.david.rosero.fund_management_service.controller;

import com.david.rosero.fund_management_service.dto.FundResponse;
import com.david.rosero.fund_management_service.model.Fund;
import com.david.rosero.fund_management_service.service.FundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @GetMapping
    public List<FundResponse> list() {
        return fundService.listFunds().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private FundResponse toResponse(Fund fund) {
        return FundResponse.builder()
                .code(fund.getCode())
                .name(fund.getName())
                .minimumAmount(fund.getMinimumAmount())
                .category(fund.getCategory())
                .build();
    }
}
