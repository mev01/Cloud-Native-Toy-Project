package com.web.curation.interceptor;

import com.web.curation.member.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    private static final String HEADER_AUTH = "Authorization";
    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        if (url.contains("swagger") || url.contains("api-docs") || url.contains("webjars")) {
            return true;
        }
        final String token = request.getHeader("access-token");
        if (StringUtils.equals(request.getMethod(), "OPTIONS")) {
            log.debug("if request options method is options, return true");
            return true;
        }
        if (token != null && jwtService.isUsable(token)) {
            log.info("Token Authorization Success");
            return true;
        } else {
            log.warn("Token Not Found");
            return false;
        }
    }
}
