package org.inflearngg.aop.security.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.token.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final JwtTokenProvider jwtTokenProvider;

    //jwt 검증은 하나 검증 가능한 곳
    private static final String[] WHITE_LIST = {
            "/api/duo/"
    };
    // 무조건 jwt가 있어야 하는 곳
    private static final String[] MEMBER_LIST = {
            "/api/site/update-password",
            "/api/site/reset-password",
            "/api/member/"

    };


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("doFilterInternal");
        log.info("request : {}", request.getRequestURI());

        if (isMemberList(request) || isWhiteList(request)) {
            try {
                String authToken = resolveToken(request);

                if (StringUtils.hasText(authToken) && jwtTokenProvider.validateToken(authToken)) {
                    String memberId = getUserIdFromToken(authToken);
                    request.setAttribute("memberId", memberId);
                }
            } catch (JwtException ex) {

                if (isMemberList(request)) {
                    //에러 메시지 가공
                    log.error("JwtException : {}", ex.getMessage());
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write(ex.getMessage());
                    return;
                }
                if (isWhiteList(request)) {
                    log.error("JwtException : {}", ex.getMessage());
                    request.setAttribute("memberId", -1L);
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);


    }

    private boolean isMemberList(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String white : MEMBER_LIST) {
            if (requestURI.startsWith(white)) {
                return true;
            }
        }
        return false;
    }
    private boolean isWhiteList(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String white : WHITE_LIST) {
            if (requestURI.startsWith(white)) {
                return true;
            }
        }
        return false;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        throw new JwtException("잘못된 토큰 정보입니다.");
    }

    private String getUserIdFromToken(String accessToken) {
        return jwtTokenProvider.extractSubject(accessToken);
    }

}

