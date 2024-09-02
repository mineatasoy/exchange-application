package com.openpayd.exchange.controller;

import com.openpayd.exchange.Exception.GenericWebAccessException;
import com.openpayd.exchange.model.Conversion;
import com.openpayd.exchange.model.Rate;
import com.openpayd.exchange.model.request.ConversionRequest;
import com.openpayd.exchange.model.response.ExchangeResponse;
import com.openpayd.exchange.service.ExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
//@Slf4j
@RequestMapping("api")
public class ExchangeController {

    ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    //EXCHANGE RATE API
    @GetMapping("/exchange-rate")
    @Operation(summary = "Exchange Rate", description = "Returns the currency rate according to the base and target currency")
    public ResponseEntity<Rate> getExchangeRate(@Parameter(description = "Base currency code", example = "USD") @RequestParam String base,
                                                @Parameter(description = "Target currency code", example = "EUR") @RequestParam String target) {
        Rate rate = exchangeService.getExchangeRateByBase(base, target);
        return new ResponseEntity<>(rate, HttpStatus.OK);

    }

    //CONVERSION API
    @GetMapping("/conversion")
    @Operation(summary = "Convert currency", description = "Converts the specified amount from base currency to target currency")
    public ResponseEntity<Conversion> getConversionByCurrency(@RequestBody(description = "Conversion Request", required = true) ConversionRequest conversionRequest) {
        Conversion conversion = exchangeService.getConversion(conversionRequest);
        return ResponseEntity.ok().body(conversion);
    }

    //CONVERSION HISTORY API
    @GetMapping("/conversion-history")
    @Operation(summary = "Conversion History", description = "Lists the conversion history related with the transactionId or date")
    public ResponseEntity<Page<Conversion>> getConversionHistory(@Parameter(description = "Transaction Id") @RequestParam(value = "id", required = false) String transactionId,
                                                                 @Parameter(description = "Transaction Date - Format: yyyy-MM-dd") @RequestParam(value = "date", required = false) LocalDate date,
                                                                 @Parameter(description = "Page Number") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber) {
        if (transactionId == null && date == null) {
            throw new GenericWebAccessException("TransactionId or Date must be provided.", HttpStatus.BAD_REQUEST);
        }
        Page<Conversion> conversions = exchangeService.getConversions(transactionId, date, pageNumber, 10);
        return new ResponseEntity<>(conversions, HttpStatus.OK);
    }

}
