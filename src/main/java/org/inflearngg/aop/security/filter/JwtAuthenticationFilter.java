package org.inflearngg.aop.security.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.token.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("doFilterInternal");
        try {
            String authToken = resolveToken(request);

            if (StringUtils.hasText(authToken) && jwtTokenProvider.validateToken(authToken)) {
                String memberId = getUserIdFromToken(authToken);
                request.setAttribute("memberId", memberId);
            }
            filterChain.doFilter(request, response);
        }
        catch (JwtException ex) {
            //에러 메시지 가공
            log.error("JwtException : {}", ex.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(ex.getMessage());

        }


    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String getUserIdFromToken(String accessToken) {
        return jwtTokenProvider.extractSubject(accessToken);
    }

}

