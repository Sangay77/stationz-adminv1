package com.station.setting;

import com.station.common.entity.Setting;
import com.station.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> generalSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings() {
        List<Setting> settings = new ArrayList<>();
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_SERVER));
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));
        return new EmailSettingBag(settings);
    }

}
