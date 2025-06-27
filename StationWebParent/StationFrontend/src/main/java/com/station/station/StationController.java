package com.station.station;

import com.station.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StationController {

    @Autowired
    private StationService stationService;

    @GetMapping("/")
    public String home() {
        return "station/home";
    }

    @GetMapping("/about_us")
    public String aboutUs(Model model) {
        List<User> users = stationService.stationTeam();
        model.addAttribute("userList", users);
        return "station/about_us";
    }

    @GetMapping("/commercial")
    public String commercial(Model model) {
        return "station/commercial_cleaning";
    }

    @GetMapping("/residential")
    public String residential(Model model) {
        return "station/residential_cleaning";
    }

    @GetMapping("/office")
    public String office(Model model) {
        return "station/office_cleaning";
    }

}
