package org.example.portier_digital_admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PortierDigitalAdminApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(PortierDigitalAdminApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PortierDigitalAdminApplication.class);
    }
}
