package org.mediagate.auth.service;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {
    String syncUsersWithKeycloak();

    List<UserRepresentation> getAllUsersFromKeycloak();

    List<RoleRepresentation> getAllRolesFromKeycloak();

    List<GroupRepresentation> getAllGroupsFromKeycloak();

    String getUserRoles(String keycloakUserId);

    String getUserGroups(String keycloakUserId);
}
