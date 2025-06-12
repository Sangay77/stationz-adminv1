package com.station.station;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StationController {

    @GetMapping("/")
    public String aboutUs(){
        return "station/home";
    }
}
