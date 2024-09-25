package com.ecom.common.entity;

import java.util.List;

public class SettingPagkage {
    private List<Setting> listSetting;

    public SettingPagkage(List<Setting> listSetting) {
        this.listSetting = listSetting;
    }

    public Setting getSetting(String key){
        Integer index = listSetting.indexOf(new Setting(key));
        Setting setting = listSetting.get(index);

        if (setting == null) return null;

        return setting;
    }

    public String getValueSetting(String key){
        Setting setting = getSetting(key);
        if (setting == null) return null;
        return setting.getValue();
    }

    public void updateSetting(String key, String value){
        Setting setting = getSetting(key);
        if(setting != null && value != null){
            setting.setValue(value);
        }
    }

    public List<Setting> getListSetting() {
        return listSetting;
    }
}
