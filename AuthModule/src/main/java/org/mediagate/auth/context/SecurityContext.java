package org.mediagate.auth.context;

import org.mediagate.auth.model.UserInfo;
import org.mediagate.auth.service.UserSecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SecurityContext {
    private static UserSecurityService securityService;

    protected SecurityContext(UserSecurityService userSecurityService) {
        SecurityContext.securityService = userSecurityService;
    }

    public static UserInfo currentUser() {
        return SecurityContext.securityService.loggedUser();
    }

    public static Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticated() {
        Authentication authentication = authentication();
        return Objects.nonNull(authentication) && authentication.isAuthenticated();
    }
}
