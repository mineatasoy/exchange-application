package com.openpayd.exchange.service;

import com.openpayd.exchange.model.Conversion;
import com.openpayd.exchange.model.Rate;
import com.openpayd.exchange.model.request.ConversionRequest;
import com.openpayd.exchange.model.response.ExchangeResponse;
import com.openpayd.exchange.repository.ConversionRepository;
import com.openpayd.exchange.service.impl.ExchangeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.ResponseSpec responseMock;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;

    @Mock
    private ConversionRepository conversionRepository;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    @Test
    void getExchangeRateByBaseTest() {

        //When
        ExchangeResponse response = ExchangeResponse.builder().base("USD").rates(new HashMap<>() {{
            put("EUR", 0.1);
        }}).build();
        Rate rate = Rate.builder().currency("EUR").rate(0.1).build();

        //Mock
        when(webClient.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.headers(any())).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(ExchangeResponse.class)).thenReturn(Mono.just(response));

        //Test
        Rate result = exchangeService.getExchangeRateByBase("USD", "EUR");
        Assertions.assertEquals(rate, result);

    }

    @Test
    void getConversionTest() {

        //Given
        String base = "USD";
        String target = "EUR";
        double rate = 0.1;
        double amount = 1000;
        Conversion conversion = Conversion.builder().transactionId(LocalDate.now() + "-" + UUID.randomUUID()).build();
        ConversionRequest request = ConversionRequest.builder().base(base).target(target).amount(amount).build();
        ExchangeResponse response = ExchangeResponse.builder().base("USD").rates(new HashMap<>() {{
            put("EUR", 0.1);
        }}).build();

        //Mock
        when(webClient.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.headers(any())).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(ExchangeResponse.class)).thenReturn(Mono.just(response));


        when(conversionRepository.save(any(Conversion.class))).thenReturn(conversion);

        // Test
        double expectedAmount = amount * rate;
        double actualAmount = exchangeService.getConversion(request).getConversionCalculated();
        Assertions.assertEquals(expectedAmount, actualAmount);

    }

    /*
    @Test
    void getConversionTest() {

        String base = "USD";
        String target = "EUR";
        double rate = 0.1;
        double amount = 1000;

        ExchangeResponse response = ExchangeResponse.builder().base("USD").rates(new HashMap<>() {{
            put("EUR", 0.1);
        }}).build();

        //Mock
        when(webClient.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.headers(any())).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(ExchangeResponse.class)).thenReturn(Mono.just(response));

        Assertions.assertEquals(exchangeService.getConversion(base, target, amount), amount * rate);


    }
    */


}
