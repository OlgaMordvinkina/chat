package org.mediagate.auth.service.impl;

import org.mediagate.auth.context.SecurityContext;
import org.mediagate.auth.exceptions.AccessControlException;
import org.mediagate.auth.exceptions.AccessControlExceptionCode;
import org.mediagate.auth.model.AccessLevel;
import org.mediagate.auth.model.UserInfo;
import org.mediagate.auth.service.CheckAccessService;
import org.mediagate.auth.util.AccessLevelsUtils;
import org.mediagate.db.model.entities.ABaseEntity;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.db.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckAccessServiceImpl implements CheckAccessService {

    private final UserRepository userRepository;

    @Override
    public boolean checkAccess(@NonNull ABaseEntity entity, @NonNull UserEntity user, @NonNull AccessLevel... accessLevels) {
        Set<AccessLevel> existingLevels = AccessLevelsUtils.normalize(getLevels(entity, user));
        Set<AccessLevel> requestedLevels = AccessLevelsUtils.normalize(accessLevels);
        requestedLevels.removeAll(existingLevels);
        return requestedLevels.isEmpty();
    }

    @Override
    public boolean checkAccess(@NonNull ABaseEntity entity, @NonNull AccessLevel... accessLevels) {
        Set<AccessLevel> existingLevels = AccessLevelsUtils.normalize(getLevels(entity));
        Set<AccessLevel> requestedLevels = AccessLevelsUtils.normalize(accessLevels);
        requestedLevels.removeAll(existingLevels);
        return requestedLevels.isEmpty();
    }

    @Override
    public boolean checkAccessWithAdminRole(@NonNull ABaseEntity entity, @NonNull UserEntity user) {
        Set<AccessLevel> levels = this.getLevels(entity);
//        boolean isAdmin = userRepository.hasRole(SecurityContext.currentUser().getId(), "admin");
//        return isAdmin || !levels.isEmpty();
        return !levels.isEmpty();
    }

    @Override
    public boolean checkAccessWithAdminRole(@NonNull ABaseEntity entity) {
        return checkAccessWithAdminRole(entity, UserInfo.to(SecurityContext.currentUser()));
    }

    @Override
    public void requireAccess(@NonNull ABaseEntity entity, @NonNull UserEntity user) {
        if (!this.checkAccessWithAdminRole(entity, user)) {
            throw new AccessControlException(AccessControlExceptionCode.ACCESS_DENIED,
                    "Сущность=[" + entity.getClass().getSimpleName() + "], id=[" + entity.getId() + "]");
        }
    }

    @Override
    public void requireAccess(@NonNull ABaseEntity entity) {
        this.requireAccess(entity, UserInfo.to(SecurityContext.currentUser()));
    }

    @Override
    public Set<AccessLevel> getLevels(@NonNull ABaseEntity entity) {
        return this.getLevels(entity, UserInfo.to(SecurityContext.currentUser()));
    }

    @Override
    public Set<AccessLevel> getLevels(@NonNull ABaseEntity entity, @NonNull UserEntity user) {
        log.info("ЗАХАРДКОЖЕНО:" + Arrays.toString(AccessLevel.values()));
        return Set.of(AccessLevel.values());
//        Set<AccessLevel> levels;
//        if (entity instanceof Document) {
//            levels = rightsDataService.findAllByDocumentIdAndUserId(nonNull(entity.getId()), user.getId());
//        } else if (entity instanceof Project) {
//            levels = rightsDataService.findAllByProjectIdAndUserId(nonNull(entity.getId()), user.getId());
//        } else if (entity instanceof Task task) {
//            levels = rightsDataService.findAllByProjectIdAndUserId(nonNull(task.getParentProjectId()), user.getId());
//        } else if (entity instanceof Space space) {
//            levels = this.getLevels(this.projectDataService.findProjectBySpaceId(nonNull(space.getId())), user);
//        } else {
//            throw new AccessControlException(AccessControlExceptionCode.UNSUPPORTED_OBJECT_TYPE, "Переданный объект " + entity.getClass().getName());
//        }
//        return levels;
    }

    /** Проверяет идентификатор, по которому будут проверяться права, на 'null'. */
    private Long nonNull(Long id) {
        if (id == null) {
            throw new AccessControlException(AccessControlExceptionCode.ACCESS_DENIED,
                    "Идентификатор, по которому определяются права, не может быть null.");
        }
        return id;
    }
}
