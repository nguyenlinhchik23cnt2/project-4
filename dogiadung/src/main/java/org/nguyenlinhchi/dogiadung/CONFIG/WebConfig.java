//package org.nguyenlinhchi.dogiadung.CONFIG;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Hỗ trợ cả hai đường dẫn phổ biến
//        registry.addResourceHandler("/uploads/products/**")
//                .addResourceLocations("file:" + uploadDir + "/");
//
////        registry.addResourceHandler("/images/products/**")
////                .addResourceLocations("file:" + uploadDir + "/");
//
//        // Debug: cho phép truy cập trực tiếp
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:D:/project4/uploads/");
//    }
//}

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
        String location = "file:" + uploadDir + "/";

        // Hỗ trợ nhiều đường dẫn frontend có thể dùng
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations(location);

        registry.addResourceHandler("/images/products/**")
                .addResourceLocations(location);

        // Debug - cho phép truy cập trực tiếp (rất hữu ích khi test)
        registry.addResourceHandler("/uploads/**")
<<<<<<< HEAD
                .addResourceLocations("file:C:/projec-t4/uploads/");
=======
                .addResourceLocations("file:D:/project-4/uploads/");
>>>>>>> 931edeeeb1e324e527ae9e0feee350a772cd7ed6
    }
}