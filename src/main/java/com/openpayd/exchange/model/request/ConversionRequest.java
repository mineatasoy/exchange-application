package com.openpayd.exchange.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionRequest {

    private String base;
    private String target;
    private double amount;


}
