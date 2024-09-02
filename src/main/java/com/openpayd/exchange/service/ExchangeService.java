package com.openpayd.exchange.service;

import com.openpayd.exchange.model.Conversion;
import com.openpayd.exchange.model.Rate;
import com.openpayd.exchange.model.request.ConversionRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ExchangeService {

    public Rate getExchangeRateByBase(String base, String target);

    public Conversion getConversion(ConversionRequest conversionRequest);

    public Page<Conversion> getConversions(String id, LocalDate date, int page, int size);

}
