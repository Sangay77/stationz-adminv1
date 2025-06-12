package com.station.admin.setting;

import com.station.common.entity.Setting;
import com.station.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, String> {

    List<Setting> findByCategory(SettingCategory category);
}
