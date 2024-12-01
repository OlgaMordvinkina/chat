package org.mediagate.auth.config;

import org.mediagate.auth.util.KeycloakJwtAttributeName;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Преобразование атрибутов JWT токена Keycloak.
 */
public class KeycloakJwtAttributes {

    private final KeycloakJwtAttributeName PRINCIPAL_ATTRIBUTE;

    Map<String, Object> attributes;

    public KeycloakJwtAttributes(JwtAuthenticationToken token, KeycloakJwtAttributeName principalAttribute) {
        this.attributes = token.getTokenAttributes();
        this.PRINCIPAL_ATTRIBUTE = principalAttribute;
    }

    public KeycloakJwtAttributes(JwtAuthenticationToken token) {
        this.attributes = token.getTokenAttributes();
        this.PRINCIPAL_ATTRIBUTE = KeycloakJwtAttributeName.PREFERRED_USERNAME;
    }

    public String firstName() {
        return getAttribute(KeycloakJwtAttributeName.FIRST_NAME);
    }

    public String lastName() {
        return getAttribute(KeycloakJwtAttributeName.LAST_NAME);
    }

    public String email() {
        return getAttribute(KeycloakJwtAttributeName.EMAIL);
    }

    public String username() {
        return getAttribute(PRINCIPAL_ATTRIBUTE);
    }

    public List<String> groups() {
        List<String> rawGroups = getAttribute(KeycloakJwtAttributeName.GROUPS);
        return Objects.isNull(rawGroups) ? List.of() : rawGroups.stream()
                .map(raw -> raw.substring(1))
                .map(raw -> raw.replace('/', '.'))
                .toList();
    }

    private <T> T getAttribute(KeycloakJwtAttributeName name) {
        return getAttribute(name.getValue());
    }

    /**
     * @return If key is null return null, if value is null return null, else return value
     */
    private <T> T getAttribute(String key) {
        if (Objects.isNull(key)) return null;

        @SuppressWarnings("unchecked")
        T value = (T) attributes.get(key);
        return Objects.isNull(value) ? null : value;
    }
}
