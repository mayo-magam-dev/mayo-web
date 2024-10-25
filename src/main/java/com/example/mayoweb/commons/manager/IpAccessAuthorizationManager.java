package com.example.mayoweb.commons.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpAccessAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        HttpServletRequest request = ((RequestAuthorizationContext) object).getRequest();
        String clientIp = getClientIp(request);

        Set<String> allowedDomains = new HashSet<>(Arrays.asList("mayomagam.store", "www.mayomagam.store", "localhost:3000"));
        Set<String> allowedIps = resolveDomainToIp(allowedDomains);
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

    private Set<String> resolveDomainToIp(Set<String> allowedDomains) {
        Set<String> resolvedIps = new HashSet<>();

        for (String domain : allowedDomains) {
            try {
                InetAddress[] addresses = InetAddress.getAllByName(domain);
                for (InetAddress address : addresses) {
                    resolvedIps.add(address.getHostAddress());
                }
            } catch (UnknownHostException e) {
                log.error("Domain not found: {}", domain);
            }
        }
        return resolvedIps;
    }
}
