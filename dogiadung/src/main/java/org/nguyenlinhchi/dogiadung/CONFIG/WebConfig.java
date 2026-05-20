package org.nguyenlinhchi.dogiadung.CONFIG;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Hỗ trợ cả hai đường dẫn phổ biến
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + uploadDir + "/");

        registry.addResourceHandler("/images/products/**")
                .addResourceLocations("file:" + uploadDir + "/");

        // Debug: cho phép truy cập trực tiếp
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/projec-t4/uploads/");
    }
}