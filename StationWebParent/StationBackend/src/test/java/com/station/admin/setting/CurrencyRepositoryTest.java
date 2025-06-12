package com.station.admin.setting;

import com.station.common.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CurrencyRepositoryTest {

    @Autowired private CurrencyRepository currencyRepository;

    @Test
    public void testCreateCurrency(){

        List<Currency> currencies = Arrays.asList(
                new Currency("United States Dollar", "$", "USD"),
                new Currency("Euro", "€", "EUR"),
                new Currency("British Pound", "£", "GBP"),
                new Currency("Japanese Yen", "¥", "JPY"),
                new Currency("Swiss Franc", "CHF", "CHF"),
                new Currency("Canadian Dollar", "$", "CAD"),
                new Currency("Australian Dollar", "$", "AUD"),
                new Currency("Indian Rupee", "₹", "INR"),
                new Currency("Chinese Yuan", "¥", "CNY"),
                new Currency("Singapore Dollar", "$", "SGD"),
                new Currency("Hong Kong Dollar", "$", "HKD"),
                new Currency("South Korean Won", "₩", "KRW"),
                new Currency("Norwegian Krone", "kr", "NOK"),
                new Currency("Swedish Krona", "kr", "SEK"),
                new Currency("Danish Krone", "kr", "DKK"),
                new Currency("Brazilian Real", "R$", "BRL"),
                new Currency("Mexican Peso", "$", "MXN"),
                new Currency("South African Rand", "R", "ZAR"),
                new Currency("Thai Baht", "฿", "THB"),
                new Currency("Bhutanese Ngultrum", "Nu.", "BTN")
        );

        currencyRepository.saveAll(currencies);

    }

    @Test
    public void testListAll(){
        List<Currency> allByOrderByNameAsc = currencyRepository.findAllByOrderByNameAsc();
        allByOrderByNameAsc.stream().forEach(System.out::println);
    }

}