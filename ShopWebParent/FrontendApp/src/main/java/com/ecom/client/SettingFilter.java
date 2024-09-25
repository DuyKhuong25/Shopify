package com.ecom.client;

import com.ecom.client.service.SettingService;
import com.ecom.common.Constants;
import com.ecom.common.entity.Setting;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SettingFilter implements Filter {
    @Autowired
    private SettingService settingService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String url = request.getRequestURL().toString();

        if(url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".jpeg")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        List<Setting> settings = settingService.getSettings();

        settings.forEach(setting -> {
            servletRequest.setAttribute(setting.getKey(), setting.getValue());
        });

        request.setAttribute("S3_URI", Constants.S3_URI);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
