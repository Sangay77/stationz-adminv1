package com.station.admin.global;

import com.station.admin.station.FooterInfoService;
import com.station.common.entity.FooterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalFooterAdvice {

    @Autowired
    private FooterInfoService footerInfoService;

    @ModelAttribute("footerInfo")
    public FooterInfo getFooterInfo() {
        return footerInfoService.getFooter();
    }
}

