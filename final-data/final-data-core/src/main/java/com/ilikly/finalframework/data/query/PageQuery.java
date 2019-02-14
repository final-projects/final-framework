package com.ilikly.finalframework.data.query;

/**
 * @author likly
 * @version 1.0
 * @date 2019-02-12 12:41:24
 * @since 1.0
 */
public class PageQuery implements IQuery{

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_SIZE = 20;

    private Integer page = DEFAULT_PAGE;
    private Integer size = DEFAULT_SIZE;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
