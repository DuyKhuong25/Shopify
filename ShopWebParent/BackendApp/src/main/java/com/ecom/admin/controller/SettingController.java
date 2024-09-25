package com.ecom.admin.controller;

import com.ecom.admin.AmazonS3Utility;
import com.ecom.admin.FileUpload;
import com.ecom.admin.service.GeneralSettingPagkage;
import com.ecom.admin.service.SettingService;
import com.ecom.common.Constants;
import com.ecom.common.entity.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class SettingController {
    @Autowired
    private SettingService settingService;

    @GetMapping("/setting")
    public String setting(Model model) {
        List<Setting> settings = settingService.getSettings();
        for (Setting setting : settings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }
        model.addAttribute("S3_URI", Constants.S3_URI);

        return "setting/setting-form";
    }

    @PostMapping("/setting/save")
    public String settingSave(@RequestParam("imageUpload") MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        GeneralSettingPagkage generalSettings = settingService.listGeneralSetting();
        saveLogo(multipartFile, generalSettings);
        updateSetting(request, generalSettings.getListSetting());

        redirectAttributes.addFlashAttribute("message", "Save setting successfully!");
        return "redirect:/setting";
    }

    public void saveLogo(MultipartFile multipartFile, GeneralSettingPagkage generalSettings) throws IOException{
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String dirName = "logo-image/" + fileName;
            generalSettings.updateImage(dirName);
            String uploadDir = "logo-image/";
//            FileUpload.cleanDir(uploadDir);
//            FileUpload.saveFile(uploadDir, fileName, multipartFile);
            AmazonS3Utility.removeFolder(uploadDir);
            AmazonS3Utility.uploadFileToS3(uploadDir, fileName, multipartFile.getInputStream());
        }
    }

    @PostMapping("/setting/save_mail_server")
    public String saveMailServer(HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        List<Setting> listMailServerSetting = settingService.listMailServerSetting();
        updateSetting(request, listMailServerSetting);

        redirectAttributes.addFlashAttribute("message", "Save mail server successfully!");
        return "redirect:/setting";
    }

    @PostMapping("/setting/save_mail_verify")
    public String saveMailVerify(HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        List<Setting> listMailVerifySetting = settingService.listMailVerifySetting();
        updateSetting(request, listMailVerifySetting);

        redirectAttributes.addFlashAttribute("message", "Save mail verify template successfully!");
        return "redirect:/setting";
    }

    @PostMapping("/setting/save_mail_order")
    public String saveMailOder(HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        List<Setting> listMailOrderSetting = settingService.listMailOrderSetting();
        updateSetting(request, listMailOrderSetting);

        redirectAttributes.addFlashAttribute("message", "Save mail order template successfully!");
        return "redirect:/setting";
    }

    @PostMapping("/setting/save_payment")
    public String saveMailPayment(HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        List<Setting> listPaymentSetting = settingService.listPaymentSetting();
        updateSetting(request, listPaymentSetting);

        redirectAttributes.addFlashAttribute("message", "Save payment config successfully!");
        return "redirect:/setting";
    }


    public void updateSetting(HttpServletRequest request, List<Setting> listSetting){
        for (Setting setting : listSetting){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }

        settingService.saveAll(listSetting);
    }
}
