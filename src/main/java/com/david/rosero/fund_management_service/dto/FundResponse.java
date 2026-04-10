package com.david.rosero.fund_management_service.dto;

import com.david.rosero.fund_management_service.model.FundCategory;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class FundResponse {
    String code;
    String name;
    BigDecimal minimumAmount;
    FundCategory category;
}
