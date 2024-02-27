package org.inflearngg.aop;

import lombok.RequiredArgsConstructor;
import org.inflearngg.aop.security.filter.JwtAuthenticationFilter;
import org.inflearngg.jwt.token.JwtTokenProvider;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] DEFAULT_LIST = {
            "/docs.html"
    };

    private static final String[] WHITE_LIST = {
            "/api/oauth/**",
            "/api/oauth/kakao/callback",
            "/api/duo/post/**",
            "/api/matches/**",
            "/api/summoner/**",
            "/api/**",
            "/api/duo/**",
    };

    private static final String[] SELLER_LIST = {
            "/api/v2/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(c -> c.disable())
                .cors(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    try {
                        auth
                                .requestMatchers(WHITE_LIST).permitAll()
                                .requestMatchers(DEFAULT_LIST).permitAll()
                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                .requestMatchers(SELLER_LIST).hasRole("SELLER")
                                .anyRequest().authenticated()
                        ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
//                .exceptionHandling(c ->
//                        c.authenticationEntryPoint(/*커스텀 에러 핸들링*/).accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}