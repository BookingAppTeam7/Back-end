package com.booking.BookingApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //try {
            registry.addMapping("/**")
                    .allowedOriginPatterns("http://192.168.1.26:4200","http://localhost:4200")////OVDE STAVITE SVOJU IP ADRESU
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowCredentials(true)
                    .maxAge(3600);
        //} catch (UnknownHostException e) {
        //    throw new RuntimeException(e);
        //}
    }
}
