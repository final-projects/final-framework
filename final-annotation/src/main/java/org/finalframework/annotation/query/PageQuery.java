package org.finalframework.annotation.query;

import lombok.Data;
import org.finalframework.annotation.Pageable;

import java.io.Serializable;

/**
 * 分页查询
 *
 * @author likly
 * @version 1.0
 * @date 2019-02-12 12:41:24
 * @since 1.0
 */
@Data
public class PageQuery implements Pageable, Serializable {

    private static final long serialVersionUID = 4813020012879522797L;
    /**
     * 默认分页页码
     */
    private static final Integer DEFAULT_PAGE = 1;
    /**
     * 默认分页容量
     */
    private static final Integer DEFAULT_SIZE = 20;

    /**
     * 分页页面
     */
    @Page
    private Integer page = DEFAULT_PAGE;
    /**
     * 页面宅师
     */
    @Size
    private Integer size = DEFAULT_SIZE;
    /**
     * 是否启用Count统计
     */
    private Boolean count = Boolean.TRUE;

}
