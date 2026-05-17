package org.nguyenlinhchi.dogiadung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "org.nguyenlinhchi.dogiadung",
        "org.nguyenlinhchi.dogiadung.CONTROLLER"   // ← Thêm dòng này
})
public class DogiadungApplication {

    public static void main(String[] args) {
        SpringApplication.run(DogiadungApplication.class, args);
    }
}