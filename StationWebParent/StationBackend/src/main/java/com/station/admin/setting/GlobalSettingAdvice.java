package com.station.admin.setting;

import com.station.common.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

import java.util.List;

@ControllerAdvice
public class GlobalSettingAdvice {

    @Autowired
    private SettingService settingService;

    @ModelAttribute
    public void addGlobalSettings(Model model) {
        List<Setting> settings = settingService.listSettings();

        for (Setting setting : settings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }
    }
}

