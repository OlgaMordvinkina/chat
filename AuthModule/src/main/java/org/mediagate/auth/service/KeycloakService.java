package org.mediagate.auth.service;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {
    String syncUsersWithKeycloak();

    UserRepresentation getUserByEmail(String email);

    List<UserRepresentation> getAllUsersFromKeycloak();

    List<RoleRepresentation> getAllRolesFromKeycloak();

    List<GroupRepresentation> getAllGroupsFromKeycloak();

    List<String> getUserRoles(String keycloakUserId);

    List<String> getUserGroups(String keycloakUserId);
}
