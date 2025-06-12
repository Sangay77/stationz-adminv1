package com.station.common.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SettingBag {

    private static final Logger logger = LoggerFactory.getLogger(SettingBag.class);


    private List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings) {
        this.listSettings = listSettings;
    }

    public Setting get(String key) {
        for (Setting setting : listSettings) {
            logger.info("Checking setting key: " + setting.getKey());
            if (key.equals(setting.getKey())) {
                logger.info("Found match for key: " + key);
                return setting;
            }
        }
        logger.error("No match found match for key: " + key);
        return null;
    }


    public String getValue(String key) {
        Setting setting = get(key);
        if (setting != null) {
            return setting.getValue();
        }
        return null;
    }

    public void update(String key, String value) {
        Setting setting = get(key);
        if (setting != null && value != null) {
            setting.setValue(value);
        }
    }

    public List<Setting> list() {
        return listSettings;
    }
}
