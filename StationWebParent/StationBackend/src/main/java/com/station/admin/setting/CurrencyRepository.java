package com.station.admin.setting;

import com.station.common.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    List<Currency> findAllByOrderByNameAsc();
}
