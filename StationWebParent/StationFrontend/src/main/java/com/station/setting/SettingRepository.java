package com.station.setting;

import com.station.common.entity.Setting;
import com.station.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, String> {

    List<Setting> findByCategory(SettingCategory category);

    @Query("SELECT s FROM Setting s WHERE s.category=?1 OR s.category=?2 ")
    List<Setting> findByTwoCategories(SettingCategory catOne,SettingCategory catTwo);
}
