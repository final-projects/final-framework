package cn.com.likly.finalframework.mybatis;

import cn.com.likly.finalframework.data.annotation.Command;
import cn.com.likly.finalframework.data.domain.Query;
import cn.com.likly.finalframework.data.domain.Update;
import cn.com.likly.finalframework.data.entity.Entity;
import cn.com.likly.finalframework.data.mapping.holder.EntityHolder;
import cn.com.likly.finalframework.data.mapping.holder.PropertyHolder;
import cn.com.likly.finalframework.mybatis.mapper.AbsMapper;
import cn.com.likly.finalframework.mybatis.mapper.DefaultMapper;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author likly
 * @version 1.0
 * @date 2018-09-27 20:39
 * @since 1.0
 */
public class AbsMapperProxy<ID extends Serializable, T extends Entity<ID>, MAPPER extends AbsMapper<ID, T>> implements InvocationHandler {
    private final DefaultMapper<ID, T> defaultMapper;
    private final Object target;
    private final EntityHolder<T, ? extends PropertyHolder> holder;
    private final Object proxy;

    public AbsMapperProxy(DefaultMapper<ID, T> defaultMapper, Object target, EntityHolder<T, ? extends PropertyHolder> holder) {
        this.defaultMapper = defaultMapper;
        this.target = target;
        this.holder = holder;
        this.proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    public MAPPER getMapper() {
        return (MAPPER) proxy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Command command = method.getAnnotation(Command.class);

        if (command == null) return method.invoke(target, args);

        switch (command.value()) {
            case INSERT_ARRAY:
                switch (holder.getPrimaryKeyType()) {
                    case AUTO_INC:
                        return defaultMapper.insert(command.value(), holder, (T[]) args[0]);
                    case UUID:
                        return defaultMapper.insertUuid(command.value(), holder, (T[]) args[0]);
                    case UUIDMD5:
                        return defaultMapper.insertMd5(command.value(), holder, (T[]) args[0]);
                    case OTHER:
                        return defaultMapper.insertOther(command.value(), holder, (T[]) args[0]);
                }
                break;
            case INSERT_COLLECTION:
                switch (holder.getPrimaryKeyType()) {
                    case AUTO_INC:
                        return defaultMapper.insert(command.value(), holder, (List<T>) args[0]);
                    case UUID:
                        return defaultMapper.insertUuid(command.value(), holder, (List<T>) args[0]);
                    case UUIDMD5:
                        return defaultMapper.insertMd5(command.value(), holder, (List<T>) args[0]);
                    case OTHER:
                        return defaultMapper.insertOther(command.value(), holder, (List<T>) args[0]);
                }
                break;

            /*=========================================== UPDATE ===========================================*/

            case UPDATE_ENTITIES_ARRAY:
                return Arrays.stream((T[]) args[0])
                        .mapToInt(it -> defaultMapper.update(command.value(), holder, it))
                        .sum();
            case UPDATE_ENTITIES_COLLECTION:
                return ((Collection<T>) args[0]).stream()
                        .mapToInt(it -> defaultMapper.update(command.value(), holder, it))
                        .sum();
            case UPDATE_ID_ARRAY:
                return defaultMapper.update(command.value(), holder, (Update) args[0], (ID[]) args[1]);
            case UPDATE_ID_COLLECTION:
                return defaultMapper.update(command.value(), holder, (Update) args[0], (Collection<ID>) args[1]);
            case UPDATE_QUERY:
                return defaultMapper.update(command.value(), holder, (Update) args[0], (Query) args[1]);

            /*=========================================== DELETE ===========================================*/
            case DELETE_ID_ARRAY:
                return defaultMapper.delete(command.value(), holder, (ID[]) args[0]);
            case DELETE_ID_COLLECTION:
                return defaultMapper.delete(command.value(), holder, (List<ID>) args[0]);
            case DELETE_ENTITIES_ARRAY:
                return defaultMapper.delete(command.value(), holder, (T[]) args[0]);
            case DELETE_ENTITIES_COLLECTION:
                return defaultMapper.delete(command.value(), holder, (Collection<T>) args[0]);
            case DELETE_QUERY:
                return defaultMapper.delete(command.value(), holder, (Query) args[0]);
            /*=========================================== SELECT ===========================================*/
            case SELECT_ID_ARRAY:
                return defaultMapper.select(command.value(), holder, (ID[]) args[0]);
            case SELECT_ID_COLLECTION:
                return defaultMapper.select(command.value(), holder, (List<ID>) args[0]);
            case SELECT_QUERY:
                return defaultMapper.select(command.value(), holder, (Query) args[0]);
            case SELECT_ONE_ID:
                return defaultMapper.selectOne(command.value(), holder, (ID) args[0]);
            case SELECT_ONE_QUERY:
                return defaultMapper.selectOne(command.value(), holder, (Query) args[0]);
            case SELECT_COUNT_QUERY:
                return defaultMapper.selectCount(command.value(), holder, (Query) args[0]);
            case SELECT_COUNT:
                return defaultMapper.selectCount(command.value(), holder);
            case SELECT_IS_EXISTS_ID:
                return defaultMapper.isExists(command.value(), holder, (ID) args[0]);
            case SELECT_IS_EXISTS_QUERY:
                return defaultMapper.isExists(command.value(), holder, (Query) args[0]);

        }


        return null;


    }
}
