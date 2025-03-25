package com.vayam.ichr.config;



import com.vayam.ichr.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**") // Apply to all routes
                .excludePathPatterns("/ICHR/login", "/ICHR/register", "/css/**", "/js/**", "/images/**"); // Exclude login page & static resources
    }
}
