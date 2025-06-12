package com.station.setting;

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

    @Autowired private SettingRepository settingRepository;

    @Test
    public void findByTwoCategories(){
        List<Setting> byTwoCategories = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
        byTwoCategories.stream().forEach(System.out::println);
    }

}