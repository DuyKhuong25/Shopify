package com.ecom.client.service;

import com.ecom.common.entity.Setting;
import com.ecom.common.entity.SettingPagkage;

import java.util.List;

public class EmailSettingPagkage extends SettingPagkage {
    public EmailSettingPagkage(List<Setting> listSetting) {
        super(listSetting);
    }

    public String getHost(){
        return super.getValueSetting("MAIL_HOST");
    }

    public int getPort(){
        return Integer.parseInt(super.getValueSetting("MAIL_PORT"));
    }

    public String getUsername(){
        return super.getValueSetting("MAIL_USERNAME");
    }

    public String getPassword(){
        return super.getValueSetting("MAIL_PASSWORD");
    }

    public String getSmtpAuth(){
        return super.getValueSetting("SMTP_AUTH");
    }

    public String getSmtpSecured(){
        return super.getValueSetting("SMTP_SECURED");
    }

    public String getFromAddress(){
        return super.getValueSetting("MAIL_FROM");
    }

    public String getSenderName(){
        return super.getValueSetting("MAIL_SENDER_NAME");
    }

    public String getVerifySubject(){
        return super.getValueSetting("CUSTOMER_VERIFY_SUBJECT");
    }

    public String getVerifyContent(){
        return super.getValueSetting("CUSTOMER_VERIFY_CONTENT");
    }

    public String getOrderSubject(){
        return super.getValueSetting("ORDER_CONFIRM_SUBJECT");
    }

    public String getOrderContent(){
        return super.getValueSetting("ORDER_CONFIRM_CONTENT");
    }
}
