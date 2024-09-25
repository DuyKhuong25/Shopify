package com.ecom.admin.reponsitory.setting;

import com.ecom.admin.repository.setting.SettingRepository;
import com.ecom.common.entity.Setting;
import com.ecom.common.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
    @Autowired
    private SettingRepository settingRepository;

    @Test
    public void testCreateSetting() {
        Setting setting1 = new Setting("WEB_NAME", "Shopify", SettingCategory.GENERAL);
        Setting setting2 = new Setting("WEB_LOGO", "logo.png", SettingCategory.GENERAL);
        Setting setting3 = new Setting("WEB_EMAIL", "shopify.ecommer@gmail.com", SettingCategory.GENERAL);
        Setting setting4 = new Setting("WEB_HOTLINE", "0968668683", SettingCategory.GENERAL);
        Setting setting5 = new Setting("COMPANY_ADDRESS", "Trần Duy Hưng, Cầu Giấy, Hà Nội, Việt Nam", SettingCategory.GENERAL);

        settingRepository.saveAll(List.of(setting1, setting2, setting3, setting4, setting5));
        List<Setting> settings = settingRepository.findAll();
        for (Setting setting : settings) {
            System.out.println(setting);
        }
    }
}
