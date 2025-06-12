package com.station.admin.setting.country;


import com.station.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class CountryRestController {

    @Autowired
    private CountryRepository countryRepository;


    @GetMapping("/countries/list")
    public List<CountryDTO> listAll() {
        return countryRepository.findAllByOrderByNameAsc()
                .stream()
                .map(country -> new CountryDTO(country.getId(), country.getName(), country.getCode()))
                .collect(Collectors.toList());
    }


    @PostMapping("/countries/save")
    public String save(@RequestBody Country country) {
        return String.valueOf(countryRepository.save(country).getId());
    }

    @GetMapping("/countries/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        countryRepository.deleteById(id);
    }

}
