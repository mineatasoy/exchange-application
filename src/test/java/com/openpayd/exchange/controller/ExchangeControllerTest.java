package com.openpayd.exchange.controller;

import com.openpayd.exchange.model.Conversion;
import com.openpayd.exchange.model.request.ConversionRequest;
import com.openpayd.exchange.service.ExchangeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class ExchangeControllerTest {

    @Mock
    private ExchangeService exchangeService;

    @InjectMocks
    private ExchangeController exchangeController;

    @Test
    void getConversionTest() {
        when(exchangeService.getConversion(any(ConversionRequest.class)))
                .thenReturn(Conversion.builder().transactionId("3423232").build());
        Assertions.assertNotNull(exchangeController.getConversionByCurrency(ConversionRequest.builder().build()));
    }

}

