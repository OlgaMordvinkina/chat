package org.mediagate.auth.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KeycloakJwtAttributeName {
    PREFERRED_USERNAME("preferred_username"),
    FIRST_NAME("given_name"),
    LAST_NAME("family_name"),
    EMAIL("email"),
    FULL_NAME("name"),
    GROUPS("groups");
    //todo добавить остальные атрибуты keycloak jwt token

    private final String value;
}
