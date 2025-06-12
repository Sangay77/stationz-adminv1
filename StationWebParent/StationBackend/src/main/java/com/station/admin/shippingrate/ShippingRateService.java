package com.station.admin.shippingrate;

import com.station.admin.setting.country.CountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ShippingRateService {

    public static final int RATES_PER_PAGE=10;

    @Autowired private ShippingRateRepository shippingRateRepository;
    @Autowired private CountryRepository countryRepository;

    public void listByPage(int pageNumber){

    }
}
