package com.station.admin.station;

import com.station.common.entity.FooterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/footer")
public class FooterAdminController {


    @Autowired
    private FooterInfoService footerInfoService;

    @GetMapping
    public String showEditForm(Model model) {
        model.addAttribute("footerInfo", footerInfoService.getFooter());
        return "stationz/footer_edit";
    }

    @PostMapping("/save")
    public String saveFooter(@ModelAttribute FooterInfo footerInfo) {
        footerInfo.setId(1L);
        footerInfoService.save(footerInfo);
        return "redirect:/admin/footer?success";
    }
}
