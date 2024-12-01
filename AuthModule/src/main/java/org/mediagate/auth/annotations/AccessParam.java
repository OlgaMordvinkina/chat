package org.mediagate.auth.annotations;

import org.mediagate.auth.model.AccessLevel;
import org.mediagate.auth.model.ObjectType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Объект, для которого проверяются права */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessParam {
    /** Тип объекта в БД */
    ObjectType type() default ObjectType.CHAT;

    /**  */
    AccessLevel[] rights() default AccessLevel.EDIT;
}
