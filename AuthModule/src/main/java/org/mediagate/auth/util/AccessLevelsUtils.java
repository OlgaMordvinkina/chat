package org.mediagate.auth.util;

import org.mediagate.auth.model.AccessLevel;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class AccessLevelsUtils {

    private static final Set<AccessLevel> ADMIN_NORMALIZED_SET = Set.of(AccessLevel.values());
    private static final Set<AccessLevel> EDIT_NORMALIZED_SET = Set.of(AccessLevel.EDIT, AccessLevel.READ);
    private static final Set<AccessLevel> READ_NORMALIZED_SET = Set.of(AccessLevel.READ);

    public Set<AccessLevel> normalize(Set<AccessLevel> accessLevels) {
        Set<AccessLevel> result = new HashSet<>();
        accessLevels.forEach(level -> {
            switch (level) {
                case ADMIN -> result.addAll(ADMIN_NORMALIZED_SET);
                case EDIT -> result.addAll(EDIT_NORMALIZED_SET);
                case READ -> result.addAll(READ_NORMALIZED_SET);
            }
        });
        return result;
    }

    public Set<AccessLevel> normalize(List<AccessLevel> accessLevels) {
        return normalize(new HashSet<>(accessLevels));
    }

    public Set<AccessLevel> normalize(AccessLevel... accessLevels) {
        return normalize(Arrays.asList(accessLevels));
    }
}
