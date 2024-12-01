package org.mediagate.auth.service;

import org.mediagate.auth.model.UserInfo;
import org.mediagate.db.model.entities.UserEntity;

public interface UserSecurityService {
    void syncUser(UserInfo user);

    UserInfo loggedUser();

    UserEntity loggedDbUser();
}
