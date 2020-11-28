package org.ifinal.finalframework.context.user;

import org.ifinal.finalframework.annotation.IUser;
import org.springframework.lang.Nullable;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface UserContext<USER extends IUser<?>> {

    /**
     * return the user.
     *
     * @return the user.
     */
    @Nullable
    USER getUser();


}