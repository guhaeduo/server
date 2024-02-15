package org.inflearngg.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://183.101.104.237:3000") // 명인님 IP 주소 필요!
                .allowedMethods("*") // 허용할 HTTP 메소드
                .maxAge(3600)
                .allowCredentials(true);
    }
}
