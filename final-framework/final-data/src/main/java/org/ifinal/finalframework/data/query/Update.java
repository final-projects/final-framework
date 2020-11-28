package org.ifinal.finalframework.data.query;

import lombok.NonNull;
import org.ifinal.finalframework.util.stream.Streamable;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Update extends Streamable<UpdateSetOperation>, Iterable<UpdateSetOperation>, SqlNode {

    static Update update() {
        return UpdateImpl.update();
    }

    static Update update(UpdateSetOperation... updateSets) {
        return UpdateImpl.update(Arrays.asList(updateSets));
    }

    static Update update(Collection<UpdateSetOperation> updateSets) {
        return UpdateImpl.update(updateSets);
    }

    Update set(@NonNull QProperty<?> property, @NonNull Object value);

    Update inc(@NonNull QProperty<?> property);

    Update incr(@NonNull QProperty<?> property, @NonNull Number value);

    Update dec(@NonNull QProperty<?> property);

    Update decr(@NonNull QProperty<?> property, @NonNull Number value);


    @Override
    default void apply(StringBuilder sql, String expression) {
        sql.append("<set>");
        this.forEach(item -> item.apply(sql, expression));
        sql.append("</set>");
    }
}