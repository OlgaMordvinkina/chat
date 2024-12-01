package org.mediagate.auth.service;

import org.mediagate.auth.model.AccessLevel;
import org.mediagate.db.model.entities.ABaseEntity;
import org.mediagate.db.model.entities.UserEntity;
import org.springframework.lang.NonNull;
import org.mediagate.auth.exceptions.AccessControlException;

import java.util.Set;

/** Сервис для проверки уровней доступа. */
public interface CheckAccessService {

    /** Проверяет наличие у пользователя запрашиваемых уровней доступа к указанной сущности.
     * Сравнивает уровни доступа, назначенные пользователю, с запрашиваемыми уровнями. Если все запрашиваемые уровни
     * присутствуют в существующих уровнях доступа, метод возвращает true.
     *
     * @param entity       Сущность, для которой проверяются права доступа.
     * @param user         Пользователь, для которого проверяются права доступа.
     * @param accessLevels Один или несколько запрашиваемых уровней доступа.
     * @return true, если у пользователя есть все запрашиваемые уровни доступа, иначе false.
     * @throws AccessControlException Если тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    boolean checkAccess(@NonNull ABaseEntity entity, @NonNull UserEntity user, @NonNull AccessLevel... accessLevels);

    /** Проверяет наличие у текущего пользователя запрашиваемых уровней доступа к указанной сущности
     * Сравнивает уровни доступа, назначенные текущему пользователю, с запрашиваемыми уровнями.
     * Если все запрашиваемые уровни присутствуют в существующих уровнях доступа, метод возвращает true.
     *
     * @param entity       Сущность, для которой проверяются права доступа.
     * @param accessLevels Один или несколько запрашиваемых уровней доступа.
     * @return true, если у текущего пользователя есть все запрашиваемые уровни доступа, иначе false.
     * @throws AccessControlException Если тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    boolean checkAccess(@NonNull ABaseEntity entity, @NonNull AccessLevel... accessLevels);

    /** Проверяет, является ли указанный пользователь администратором или есть ли у него
     * права доступа к указанной сущности. Возвращает true, если пользователь - администратор или у него есть права доступа,
     * иначе false.
     *
     * @param entity Сущность, для которой проверяется доступ.
     * @return true, если текущий пользователь - администратор и у него есть доступ к сущности, иначе false.
     * @throws AccessControlException Если тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    boolean checkAccessWithAdminRole(@NonNull ABaseEntity entity, @NonNull UserEntity user);

    /** Проверяет, является ли текущий пользователь администратором или есть ли у него
     * права доступа к указанной сущности. Возвращает true, если пользователь - администратор или у него есть права доступа,
     * иначе false.
     *
     * @param entity Сущность, для которой проверяется доступ.
     * @return true, если текущий пользователь - администратор и у него есть доступ к сущности, иначе false.
     * @throws AccessControlException Если тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    boolean checkAccessWithAdminRole(@NonNull ABaseEntity entity);

    /** Гарантирует наличие у пользователя необходимых уровней доступа к указанной сущности.
     * Если у пользователя нет требуемых уровней доступа, выбрасывается AccessControlException.
     *
     * @param entity Сущность, для которой проверяются права доступа.
     * @param user   Пользователь, для которого проверяются права доступа.
     * @throws AccessControlException Если у пользователя нет необходимых уровней доступа
     * или тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    void requireAccess(@NonNull ABaseEntity entity, @NonNull UserEntity user);

    /** Гарантирует наличие у текущего пользователя необходимых уровней доступа к указанной сущности.
     * Если у пользователя нет требуемых уровней доступа, выбрасывается AccessControlException.
     *
     * @param entity Сущность, для которой проверяются права доступа.
     * @throws AccessControlException Если у пользователя нет необходимых уровней доступа
     * или тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    void requireAccess(@NonNull ABaseEntity entity);

    /** Возвращает набор уровней доступа для текущего пользователя к указанной сущности.
     *
     * @param entity Сущность, для которой проверяются права доступа.
     * @return Набор уровней доступа (AccessLevel) пользователя для указанной сущности.
     * @throws AccessControlException Если тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    Set<AccessLevel> getLevels(ABaseEntity entity);

    /** Возвращает набор уровней доступа пользователя к указанной сущности.
     *
     * @param entity Сущность, для которой проверяются права доступа.
     * @param user   Пользователь, для которого проверяются права доступа.
     * @return Набор уровней доступа (AccessLevel) пользователя для указанной сущности.
     * @throws AccessControlException Если тип переданной сущности не поддерживается.<br>
     * Поддерживаются: Document, Project, Task или Space.
     */
    Set<AccessLevel> getLevels(ABaseEntity entity, UserEntity user);
}
