package com.openpayd.exchange.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
public class Conversion {

    @Id
    String transactionId;
    LocalDate date;
    String base;
    String target;
    double amount;
    double rate;
    double conversionCalculated;


}
