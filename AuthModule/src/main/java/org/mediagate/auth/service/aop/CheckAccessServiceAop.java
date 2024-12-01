package org.mediagate.auth.service.aop;

import org.mediagate.auth.annotations.AccessParam;
import org.mediagate.auth.model.AclObjectIdentity;
import org.mediagate.db.model.entities.UserEntity;

public interface CheckAccessServiceAop {
    boolean checkAccess(AclObjectIdentity objectIdentity, AccessParam accessParam, UserEntity userInfo);
}
