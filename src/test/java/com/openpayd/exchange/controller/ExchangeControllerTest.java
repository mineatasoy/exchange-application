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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class ExchangeControllerTest {

    @Mock
    private ExchangeService exchangeService;

    @InjectMocks
    private ExchangeController exchangeController;

    @Test
    void getExchangeRateTest() {

    }

    @Test
    void getConversionTest() {
        when(exchangeService.getConversion(any(ConversionRequest.class)))
                .thenReturn(Conversion.builder().transactionId("3423232").build());
        Assertions.assertNotNull(exchangeController.getConversionByCurrency(ConversionRequest.builder().build()));
    }

    @Test
    void getConversionHistory() {
        when(exchangeService.getConversions(anyString(), any(LocalDate.class), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of(Conversion.builder().transactionId("3423232").build()), Pageable.ofSize(1), 10));
        Assertions.assertNotNull(exchangeController.getConversionHistory("3423232", LocalDate.now(), 1));
    }
}

