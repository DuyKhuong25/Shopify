package com.ecom.common.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @Column(name = "`key`", nullable = false, length = 130)
    private String key;

    @Column(name = "value", nullable = false, length = 1500)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 45, nullable = false)
    private SettingCategory category;

    public Setting() {}

    public Setting(String key){
        this.key = key;
    }

    public Setting(String key, String value, SettingCategory category) {
        this.key = key;
        this.value = value;
        this.category = category;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        return Objects.equals(key, setting.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    @Override
    public String toString() {
        return "Setting [key=" + key + ", value=" + value + "]";
    }

}
