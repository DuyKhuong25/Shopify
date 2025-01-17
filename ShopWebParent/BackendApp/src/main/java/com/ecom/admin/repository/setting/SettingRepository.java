package com.ecom.admin.repository.setting;

import com.ecom.common.entity.Setting;
import com.ecom.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {
    List<Setting> findByCategory(SettingCategory category);
}
