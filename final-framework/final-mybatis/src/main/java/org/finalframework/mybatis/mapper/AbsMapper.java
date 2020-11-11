package org.finalframework.mybatis.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.finalframework.annotation.IEntity;
import org.finalframework.annotation.IQuery;
import org.finalframework.data.query.Update;
import org.finalframework.data.repository.Repository;
import org.finalframework.mybatis.sql.provider.*;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author likly
 * @version 1.0
 * @date 2018-09-27 17:49
 * @since 1.0
 */
@SuppressWarnings("all")
public interface AbsMapper<ID extends Serializable, T extends IEntity<ID>> extends Repository<ID, T> {

    /**
     * @param table 表名
     * @param view 视图,
     * @param ignore 是否忽略重复数据,{@literal INSERT IGNORE}
     * @param entities 实体集
     * @see InsertSqlProvider#insert(ProviderContext, Map)
     */
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "list.id", keyColumn = "id")
    @InsertProvider(InsertSqlProvider.class)
    int insert(@Param("table") String table, @Param("view") Class<?> view, @Param("ignore") boolean ignore,
               @Param("list") Collection<T> entities);

    /**
     * @param table 表名
     * @param view 视图
     * @param entities 实体集
     * @see InsertSqlProvider#replace(ProviderContext, Map)
     */
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "list.id", keyColumn = "id")
    @InsertProvider(InsertSqlProvider.class)
    int replace(@Param("table") String table, @Param("view") Class<?> view, @Param("list") Collection<T> entities);

    /**
     * @param table 表名
     * @param view 视图
     * @param entities 实体集
     * @see InsertSqlProvider#save(ProviderContext, Map)
     */
    @Override
    @Options(useGeneratedKeys = true, keyProperty = "list.id", keyColumn = "id")
    @InsertProvider(InsertSqlProvider.class)
    int save(@Param("table") String table, @Param("view") Class<?> view, @Param("list") Collection<T> entities);


    @Override
    @UpdateProvider(UpdateSqlProvider.class)
    int update(@Param("table") String table, @Param("view") Class<?> view,
               @Param("entity") T entity, @Param("update") Update update, @Param("selective") boolean selective,
               @Param("ids") Collection<ID> ids, @Param("query") IQuery query);

    @Override
    @DeleteProvider(DeleteSqlProvider.class)
    int delete(@Param("table") String table, @Param("ids") Collection<ID> ids, @Param("query") IQuery query);

    @Override
    @SelectProvider(SelectSqlProvider.class)
    List<T> select(@Param("table") String table, @Param("view") Class<?> view, @Param("ids") Collection<ID> ids,
                   @Param("query") IQuery query);

    @Override
    @SelectProvider(SelectSqlProvider.class)
    T selectOne(@Param("table") String table, @Param("view") Class<?> view, @Param("id") ID id, @Param("query") IQuery query);

    @Override
    @SelectProvider(SelectCountSqlProvider.class)
    long selectCount(@Param("table") String table, @Param("ids") Collection<ID> ids, @Param("query") IQuery query);
}