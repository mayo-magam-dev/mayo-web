package com.example.mayoweb.commons.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String clientIp = getClientIp(request);
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String userAgent = request.getHeader("User-Agent");

        log.info("[Request] IP: {}, Method: {}, URL: {}, User-Agent: {}", clientIp, method, requestUrl, userAgent);
        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
