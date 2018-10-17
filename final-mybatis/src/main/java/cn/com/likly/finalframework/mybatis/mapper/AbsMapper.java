package cn.com.likly.finalframework.mybatis.mapper;

import cn.com.likly.finalframework.data.entity.Entity;
import cn.com.likly.finalframework.data.repository.Repository;

import java.io.Serializable;

/**
 * @author likly
 * @version 1.0
 * @date 2018-09-27 17:49
 * @since 1.0
 */
@SuppressWarnings("all")
public interface AbsMapper<ID extends Serializable, T extends Entity<ID>> extends Repository<ID, T> {


}
