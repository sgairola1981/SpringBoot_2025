package com.gairola.imageai.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map /sd-images/** to files under uploads/sd/
        registry.addResourceHandler("/sd-images/**")
                .addResourceLocations("file:uploads/sd/");
    }
}