package com.station.admin.setting;

import com.station.admin.setting.country.CountryRepository;
import com.station.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class CountryRepositoryTest {

    @Autowired private CountryRepository countryRepository;

    @Test
    public void addCountry(){

        Country BHUTAN=new Country("BHUTAN","BT");
        Country INDIA=new Country("INDIA","IN");
        Country AUS=new Country("AUSTRALIA","AU");

        countryRepository.saveAll(Arrays.asList(BHUTAN,INDIA,AUS));

    }

}