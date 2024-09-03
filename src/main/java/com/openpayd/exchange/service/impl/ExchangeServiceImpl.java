package com.openpayd.exchange.service.impl;

import com.openpayd.exchange.Exception.GenericDataAccessException;
import com.openpayd.exchange.Exception.GenericWebAccessException;
import com.openpayd.exchange.model.Conversion;
import com.openpayd.exchange.model.Rate;
import com.openpayd.exchange.model.request.ConversionRequest;
import com.openpayd.exchange.model.response.ExchangeResponse;
import com.openpayd.exchange.repository.ConversionRepository;
import com.openpayd.exchange.service.ExchangeService;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


@Service("ExchangeServiceImpl")
public class ExchangeServiceImpl implements ExchangeService {

    public static final String ERROR_WHILE_RETRIEVING_EXCHANGE_RATE = "Error while retrieving exchange rate from data.fixer.io";
    public static final String ERROR_WHILE_STORING_DATA = "Error while storing data";

    private final WebClient webClient;

    private final ConversionRepository conversionRepository;

    public ExchangeServiceImpl(ConversionRepository conversionRepository, WebClient webClient) {
        this.conversionRepository = conversionRepository;
        this.webClient = webClient;
    }

    @Override
    public Rate getExchangeRateByBase(String base, String target) {
        Rate rate = new Rate();
        double rateValue = getExchangeRatesAllByBase(base).getRates().get(target);
        rate.setCurrency(target);
        rate.setRate(rateValue);
        return rate;
    }

    private ExchangeResponse getExchangeRatesAllByBase(String base) {
        try {
            var response = webClient.get()
                    .headers(httpHeaders -> httpHeaders.setContentType(MediaType.APPLICATION_JSON))
                    .retrieve()
                    .bodyToMono(ExchangeResponse.class)
                    .block();
            return response;
        } catch (final RestClientException exception) {
            var message = ERROR_WHILE_RETRIEVING_EXCHANGE_RATE;
            throw new GenericWebAccessException(message, exception);
        }
    }

    @Override
    public Conversion getConversion(ConversionRequest conversionRequest) {
        LocalDate now = LocalDate.now();
        String transactionId = now + "-" + UUID.randomUUID().toString().substring(0, 18);
        String base = conversionRequest.getBase();
        double rate = getExchangeRatesAllByBase(base).getRates().get(conversionRequest.getTarget());
        Conversion conversion = Conversion.builder()
                .transactionId(transactionId)
                .date(now)
                .base(conversionRequest.getBase())
                .target(conversionRequest.getTarget())
                .amount(conversionRequest.getAmount())
                .rate(rate)
                .conversionCalculated(rate * conversionRequest.getAmount())
                .build();
        try {
            saveConversion(conversion);
        } catch (DataAccessException exception) {
            throw new GenericDataAccessException(ERROR_WHILE_STORING_DATA, exception);
        }
        saveConversion(conversion);
        return conversion;
    }

    @Override
    public Page<Conversion> getConversions(String transactionId, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (transactionId != null) {
            Optional<Conversion> conversionOptional = getConversionByTransactionId(transactionId);
            if (conversionOptional.isPresent()) {
                return new PageImpl<>(Collections.singletonList(conversionOptional.get()), pageable, 1);
            } else {
                return Page.empty(pageable);
            }
        } else if (date != null) {
            return getConversionByTransactionDate(date, pageable);
        } else {
            return Page.empty(pageable);
        }
    }

    private void saveConversion(Conversion conversion) {
        conversionRepository.save(conversion);
    }

    private Optional<Conversion> getConversionByTransactionId(String transactionId) {
        return conversionRepository.findById(transactionId);
    }

    private Page<Conversion> getConversionByTransactionDate(LocalDate date, Pageable pageable) {
        return conversionRepository.findByDate(date, pageable);
    }
}
