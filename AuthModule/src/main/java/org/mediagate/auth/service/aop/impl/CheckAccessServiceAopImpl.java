package org.mediagate.auth.service.aop.impl;

import org.mediagate.auth.annotations.AccessParam;
import org.mediagate.auth.model.AclObjectIdentity;
import org.mediagate.auth.service.CheckAccessService;
import org.mediagate.auth.service.aop.CheckAccessServiceAop;
import org.mediagate.db.model.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckAccessServiceAopImpl implements CheckAccessServiceAop {

//    private final IProjectDataService projectDataService;
//    private final ISpaceDataService spaceDataService;
//    private final IDocumentDataService documentDataService;

    private final CheckAccessService checkAccessService;

    @Transactional(readOnly = true)
    public boolean checkAccess(AclObjectIdentity objectIdentity, AccessParam accessParam, UserEntity user) {
        log.info("ЗАХАРДКОЖЕНО true");
        return true;
//        final Long resourceId = objectIdentity.getId();
//        final ObjectType type = objectIdentity.getType();
//        ABaseEntity entity = ResourceThrowableUtils.get(resourceId, type, resourceDataService(type));
//        return checkAccessService.checkAccess(entity, user, accessParam.rights());
    }

//    private IDataService<Long, ? extends ABaseEntity> resourceDataService(ObjectType type) {
//        return switch (type) {
//            case PROJECT -> projectDataService;
//            case SPACE -> spaceDataService;
//            case DOCUMENT -> documentDataService;
//            case COMMENT, TASK -> throw new AccessControlException(AccessControlExceptionCode.UNSUPPORTED_OBJECT_TYPE);
//        };
//    }

}
