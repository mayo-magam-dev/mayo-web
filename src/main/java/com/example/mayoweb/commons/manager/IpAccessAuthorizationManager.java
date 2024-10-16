package com.example.mayoweb.commons.manager;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Slf4j
@Component
public class IpAccessAuthorizationManager implements AuthorizationManager {

    @Override
    public AuthorizationDecision check(Supplier authentication, Object object) {

        HttpServletRequest request = ((RequestAuthorizationContext) object).getRequest();
        String clientIp = getClientIp(request);

        Set<String> allowedIps = new HashSet<>();
        allowedIps.add("127.0.0.1");

        return new AuthorizationDecision(allowedIps.contains(clientIp));
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            log.info("x-forwarded-for : {}", xForwardedFor.split(",")[0]);
            return xForwardedFor.split(",")[0];
        }

        log.info("remote address : {}", request.getRemoteAddr());
        return request.getRemoteAddr();
    }
}
