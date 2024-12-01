package org.mediagate.auth.model;

import org.mediagate.db.model.entities.ABaseEntity;
import lombok.Getter;

@Getter
public class AclObjectIdentity {
    ObjectType type;
    Long id;
    AclDtoIdentity dto;
    ABaseEntity entity;

    protected AclObjectIdentity() {

    }

    public static AclObjectIdentity of(ObjectType type, AclDtoIdentity dto) {
        AclObjectIdentity aclObjectIdentity = create(type, dto.getId());
        aclObjectIdentity.dto = dto;
        return aclObjectIdentity;
    }

    public static AclObjectIdentity of(ObjectType type, ABaseEntity entity) {
        AclObjectIdentity aclObjectIdentity = create(type, entity.getId());
        aclObjectIdentity.entity = entity;
        return aclObjectIdentity;
    }

    public static AclObjectIdentity of(ObjectType type, Long id) {
        return create(type, id);
    }

    private static AclObjectIdentity create(ObjectType type, Long id) {
        AclObjectIdentity aclObjectIdentity = new AclObjectIdentity();
        aclObjectIdentity.type = type;
        aclObjectIdentity.id = id;
        return aclObjectIdentity;
    }
}
