package com.david.rosero.fund_management_service.service;

import com.david.rosero.fund_management_service.model.Fund;

import java.util.List;

public interface FundService {
    List<Fund> listFunds();
    Fund getByCode(String code);
}
