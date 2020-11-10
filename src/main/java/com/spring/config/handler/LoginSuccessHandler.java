package com.spring.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException{
        handle(httpServletRequest, httpServletResponse, authentication);
        clearAuthAttr(httpServletRequest);
    }

    private void handle(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse,
                        Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(authentication);

        if (httpServletResponse.isCommitted()) {
            System.out.println("response already committed");
        }

        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, targetUrl);
    }

    private String determineTargetUrl(final Authentication authentication) {
        Map<String, String> userTargetUrlMap = new HashMap<>();
        userTargetUrlMap.put("ADMIN", "/admin/users");
        userTargetUrlMap.put("USER", "/user/userPage");

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (userTargetUrlMap.containsKey(authorityName)) {
                return userTargetUrlMap.get(authorityName);
            }
        }
        throw new IllegalStateException();
    }

    private void clearAuthAttr(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
