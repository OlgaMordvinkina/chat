package org.mediagate.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.mediagate.auth.context.SecurityContext;
import org.mediagate.auth.model.UserInfo;
import org.mediagate.auth.service.KeycloakService;
import org.mediagate.auth.service.UserSecurityService;
import org.mediagate.auth.util.KeycloakJwtAttributeName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Синхронизация текущего пользователя с БД
 */
@Component
@RequiredArgsConstructor
public class KeycloakJwtSyncFilter extends OncePerRequestFilter {
    private final KeycloakService keycloakService;
    private final UserSecurityService userSecurityService;
    @Value("${security.principal_attribute}")
    private KeycloakJwtAttributeName PRINCIPAL_ATTRIBUTE = KeycloakJwtAttributeName.PREFERRED_USERNAME;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContext.authentication();
        if (Objects.nonNull(token)) {
            KeycloakJwtAttributes attributes = new KeycloakJwtAttributes(token, PRINCIPAL_ATTRIBUTE);
            UserInfo userInfo = UserInfo.builder()
                    .email(attributes.email()) //todo email as email - preferredUsername as username
                    .firstName(attributes.firstName())
                    .lastName(attributes.lastName())
                    .groups(attributes.groups())
//                    .globalGroups(this.keycloakService.getUserGroups(token.getName()))
//                    .globalRoles(this.keycloakService.getUserRoles(token.getName()))
                    .build();

            userSecurityService.syncUser(userInfo);
        }
        filterChain.doFilter(request, response);
    }
}
