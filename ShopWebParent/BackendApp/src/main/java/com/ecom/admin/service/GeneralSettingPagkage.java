package com.ecom.admin.service;

import com.ecom.common.entity.Setting;
import com.ecom.common.entity.SettingPagkage;

import java.util.List;

public class GeneralSettingPagkage extends SettingPagkage {
    public GeneralSettingPagkage(List<Setting> settings) {
        super(settings);
    }

    public void updateImage(String value){
        super.updateSetting("WEB_LOGO", value);
    }
}
