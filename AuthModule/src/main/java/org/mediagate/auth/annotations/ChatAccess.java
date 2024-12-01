package org.mediagate.auth.annotations;

/** Метод, использующий доступы к объектам */
public @interface ChatAccess {
    String[] hasRoles() default "";
}
