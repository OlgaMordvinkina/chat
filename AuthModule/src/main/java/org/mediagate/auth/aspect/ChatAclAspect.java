package org.mediagate.auth.aspect;

import org.mediagate.auth.annotations.AccessParam;
import org.mediagate.auth.annotations.ChatAccess;
import org.mediagate.auth.context.SecurityContext;
import org.mediagate.auth.exceptions.AccessControlException;
import org.mediagate.auth.exceptions.AccessControlExceptionCode;
import org.mediagate.auth.model.AclDtoIdentity;
import org.mediagate.auth.model.AclObjectIdentity;
import org.mediagate.auth.model.UserInfo;
import org.mediagate.auth.service.aop.CheckAccessServiceAop;
import org.mediagate.db.model.entities.ABaseEntity;
import org.mediagate.db.model.entities.UserEntity;
import org.mediagate.db.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class ChatAclAspect {

    private final CheckAccessServiceAop checkAccessServiceAop;
    private final UserRepository userRepository;

    public static UserInfo getUserInfo() {
        Authentication authentication = SecurityContext.authentication();
        if (!SecurityContext.isAuthenticated()) {
            throw new AccessControlException(AccessControlExceptionCode.USER_NOT_AUTHENTICATED, authentication != null ? authentication.getName() : null);
        }
        return SecurityContext.currentUser();
    }

    @Around("@annotation(chatAccess)")
    public Object checkRights(ProceedingJoinPoint joinPoint, ChatAccess chatAccess) throws Throwable {
        return checkAcl(joinPoint, chatAccess);
    }

    public Object checkAcl(ProceedingJoinPoint joinPoint, ChatAccess chatAccess) throws Throwable {
        UserInfo userInfo = getUserInfo();

        Object[] args = joinPoint.getArgs();
        List<Pair<AccessParam, AclObjectIdentity>> accessParams = getParams(args, joinPoint.getSignature());
        if (accessParams.isEmpty()) {
            throw new AccessControlException(
                    AccessControlExceptionCode.ANNOTATION_INVALID_USAGE, "Необходим хотя бы один параметр с аннотацией AccessParam"
            );
        }

        final UserEntity user = Optional.ofNullable(userRepository.findByEmail(userInfo.getEmail()))
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с email" + "[ " + userInfo.getEmail() + " ] не найден"));

        for (Pair<AccessParam, AclObjectIdentity> accessParam : accessParams) {
            AclObjectIdentity aclObjectIdentity = accessParam.getRight();
            if (!checkAccessServiceAop.checkAccess(accessParam.getRight(), accessParam.getLeft(), user)) {
                throw new AccessControlException(
                        AccessControlExceptionCode.ACCESS_DENIED,
                        String.format("Пользователь с именем/электронной почтой не имеет доступа к объекту id - %d, type - %s", aclObjectIdentity.getId(), aclObjectIdentity.getType())
                );
            }
        }
        return joinPoint.proceed(args);
    }



    public List<Pair<AccessParam, AclObjectIdentity>> getParams(@NonNull Object[] args, @NonNull Signature signature) {
        MethodSignature methodSignature = (MethodSignature) signature;
        Parameter[] params = methodSignature.getMethod().getParameters();
        List<Pair<AccessParam, AclObjectIdentity>> aclParams = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            Parameter param = params[i];
            Object arg = args[i];

            AccessParam accessParam = param.getAnnotation(AccessParam.class);
            if (Objects.nonNull(accessParam)) {
                if (arg instanceof AclDtoIdentity aclDtoIdentity) {
                    aclParams.add(Pair.of(accessParam, AclObjectIdentity.of(accessParam.type(), aclDtoIdentity)));
                } else if (arg instanceof ABaseEntity entity) {
                    aclParams.add(Pair.of(accessParam, AclObjectIdentity.of(accessParam.type(), entity)));
                } else if (arg instanceof Long id) {
                    aclParams.add(Pair.of(accessParam, AclObjectIdentity.of(accessParam.type(), id)));
                } else {
                    throw new AccessControlException(
                            AccessControlExceptionCode.ANNOTATION_INVALID_USAGE,
                            String.format("Method %s must contain AclObjectIdentity or BaseEntity or Long parameter annotated AccessParam", signature.getName())
                    );
                }
            }
        }
        return aclParams;
    }

}

