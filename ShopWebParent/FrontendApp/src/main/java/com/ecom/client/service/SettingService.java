package com.ecom.client.service;

import com.ecom.client.repository.SettingRepository;
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
        List<Setting> settings = new ArrayList<Setting>();
        settings.addAll(settingRepository.findByCategory(SettingCategory.GENERAL));
        return settings;
    }

    public EmailSettingPagkage getEmailSettingPagkage() {
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));
        return new EmailSettingPagkage(settings);
    }
}
