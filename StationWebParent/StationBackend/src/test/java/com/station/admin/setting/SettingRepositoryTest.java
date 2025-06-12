package com.station.admin.setting;

import com.station.common.entity.Setting;
import com.station.common.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class SettingRepositoryTest {

    @Autowired
    private SettingRepository settingRepository;

    @Test
    public void createGeneralSetting() {

//        Setting general = new Setting("SITE_NAME", "Station", SettingCategory.GENERAL);
        Setting siteLogo = new Setting("SITE LOGO", "Station.img", SettingCategory.GENERAL);
        Setting copyRight = new Setting("COPYRIGHT", "COPYRIGHT (c) 2025 station LTD", SettingCategory.GENERAL);
        settingRepository.saveAll(List.of(siteLogo,copyRight));

    }

    @Test
    public void testListSettingByCategory(){
        List<Setting> settings=settingRepository.findByCategory(SettingCategory.GENERAL);
        settings.forEach(System.out::println);
    }

}