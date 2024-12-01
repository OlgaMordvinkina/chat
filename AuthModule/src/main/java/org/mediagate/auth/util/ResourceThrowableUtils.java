package org.mediagate.auth.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceThrowableUtils {

//    public <ID, T extends ABaseEntity> T get(ID id, ObjectType type, IDataService<ID, T> service) {
//        return ((ResourceThrowable<ID, T>) () -> service.findById(id)).getOrThrow(id, type);
//    }
//
//    @FunctionalInterface
//    interface ResourceThrowable<ID, T extends ABaseEntity<ID>> {
//        default T getOrThrow(ID id, ObjectType type) {
//            try {
//                return get();
//            } catch (EntityNotFoundException e) {
//                throw new AccessControlException(AccessControlExceptionCode.OBJECT_NOT_FOUND, "Объект с id: " + id + ", type: " + type + " не найден");
//            }
//        }
//
//        T get();
//    }
}
