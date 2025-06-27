package com.station.global;

import com.station.common.entity.FooterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GlobalFooterAdvice {

    @Autowired
    private FooterInfoService footerInfoService;

    @ModelAttribute("footerInfo")
    public FooterInfo getFooterInfo() {
        return footerInfoService.getFooter(); // e.g., from DB, ID=1
    }
}
