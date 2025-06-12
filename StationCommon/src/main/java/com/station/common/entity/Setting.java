package com.station.common.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "settings")
public class Setting {

    @Id
    @Column(name = "`key`", nullable = false, length = 128)
    private String key;

    @Column(nullable = false, length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private SettingCategory category;

    public Setting(String key, String value, SettingCategory settingCategory) {
        this.key = key;
        this.value = value;
        this.category = settingCategory;
    }

    public Setting() {
    }

    public Setting(String key) {

        this.key = key;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SettingCategory getCategory() {
        return category;
    }

    public void setCategory(SettingCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Setting setting)) return false;
        return Objects.equals(key, setting.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
