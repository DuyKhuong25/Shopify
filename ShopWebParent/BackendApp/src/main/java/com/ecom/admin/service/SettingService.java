package com.ecom.admin.service;

import com.ecom.admin.repository.setting.SettingRepository;
import com.ecom.common.entity.Setting;
import com.ecom.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> getSettings() {
        return settingRepository.findAll();
    }

    public GeneralSettingPagkage listGeneralSetting(){
        List<Setting> settings = new ArrayList<>();
        settings.addAll(settingRepository.findByCategory(SettingCategory.GENERAL));

        return new GeneralSettingPagkage(settings);
    }

    public List<Setting> listMailServerSetting(){
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> listMailVerifySetting(){
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> listPaymentSetting(){
        return settingRepository.findByCategory(SettingCategory.PAYMENT);
    }

    public List<Setting> listMailOrderSetting(){
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public void saveAll(Iterable<Setting> settings) {
        settingRepository.saveAll(settings);
    }

}
