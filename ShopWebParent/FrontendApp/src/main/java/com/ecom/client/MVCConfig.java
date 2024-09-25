package com.ecom.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        configImagePath("user-photo", registry);
        configImagePath("category-image", registry);
        configImagePath("brand-logo", registry);
        configImagePath("product-image", registry);
        configImagePath("logo-image", registry);
    }

    public void configImagePath(String dirName, ResourceHandlerRegistry registry){
        Path photoDir = Paths.get(dirName);
        String photoPath = photoDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + photoPath + "/");
    }
}
