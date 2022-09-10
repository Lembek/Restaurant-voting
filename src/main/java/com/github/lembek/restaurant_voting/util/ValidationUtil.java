package com.github.lembek.restaurant_voting.util;

import com.github.lembek.restaurant_voting.error.IllegalRequestDataException;
import com.github.lembek.restaurant_voting.model.BaseEntity;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

public final class ValidationUtil {

    private ValidationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            String message = entity.getClass().getSimpleName().equals("Restaurant") ?
                    "Restaurant with id=" + id + " don't have this dish" :
                    entity.getClass().getSimpleName() + " must has id=" + id;
            throw new IllegalRequestDataException(message);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    public static <T> T checkExisted(T obj, int id) {
        if (obj == null) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
        return obj;
    }

    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }
}
