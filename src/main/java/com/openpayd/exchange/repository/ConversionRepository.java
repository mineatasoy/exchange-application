package com.openpayd.exchange.repository;

import com.openpayd.exchange.model.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConversionRepository extends JpaRepository<Conversion, String> {

    Page<Conversion> findByDate(LocalDate date, Pageable pageable);


}
