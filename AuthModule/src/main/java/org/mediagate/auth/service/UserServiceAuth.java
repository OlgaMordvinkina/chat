package org.mediagate.auth.service;

import org.keycloak.representations.idm.UserRepresentation;
import org.mediagate.db.model.entities.UserEntity;

public interface UserServiceAuth {

    UserRepresentation getUserByEmailFromKeycloak(String email);

    UserEntity createUserInDB(UserRepresentation userFromKK);
}
