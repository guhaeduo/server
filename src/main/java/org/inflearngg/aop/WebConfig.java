package org.inflearngg.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","http://localhost:8080", "https://guhaeduo.github.io/client/","https://guhaeduo.github.io/") // 명인님 IP 주소 필요!
                .allowedMethods("*") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 커스텀 HTTP 헤더 (Authorization 등)
                //Access-Control-Allow-Origin:
                .exposedHeaders("Access-Control-Allow-Origin")
                .maxAge(3600)
                .allowCredentials(true);
    }
}
