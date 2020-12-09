package org.ifinal.finalframework.data.query;

import lombok.Getter;
import lombok.Setter;
import org.ifinal.finalframework.annotation.core.Pageable;
import org.ifinal.finalframework.annotation.query.Direction;
import org.ifinal.finalframework.data.query.criterion.Criterion;
import org.ifinal.finalframework.util.Asserts;
import org.ifinal.finalframework.util.stream.Streamable;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class Query implements Streamable<Criterion>, Pageable, SqlNode {

    /**
     * 页码，第一页从1开始
     */
    @Getter
    @Setter
    private Integer page;
    /**
     * 页面容量
     */
    @Setter
    @Getter
    private Integer size;
    /**
     * 是否进行Count查询
     */
    @Setter
    @Getter
    private Boolean count = Boolean.TRUE;
    @Getter
    private final List<Criterion> criteria = new ArrayList<>();
    @Getter
    private Group group;
    @Getter
    private Sort sort;
    @Getter
    private Limit limit;

    public Query page(Integer page, Integer size) {
        this.page = page;
        this.size = size;
        return this;
    }

    public Query page(Integer page) {
        this.page = page;
        return this;
    }

    public Query size(Integer size) {
        this.size = size;
        return this;
    }

    public Query count(Boolean count) {
        this.count = count;
        return this;
    }

    public Query where(@NonNull Criterion... criteria) {
        return where(Arrays.asList(criteria));
    }

    public Query where(@NonNull Collection<Criterion> criteria) {
        this.criteria.addAll(criteria);
        return this;
    }

    public Query group(QProperty<?>... properties) {
        return group(Arrays.asList(properties));
    }

    public Query group(Collection<QProperty<?>> properties) {
        this.group = Asserts.isNull(group) ? Group.by(properties) : this.group.and(Group.by(properties));
        return this;
    }

    public Query sort(@NonNull Order... orders) {
        return sort(Arrays.asList(orders));
    }

    public Query sort(Sort sort) {
        if (this.sort == null) {
            this.sort = sort;
        } else {
            this.sort.and(sort);
        }
        return this;
    }

    public Query sort(@NonNull Collection<Order> orders) {
        sort = sort == null ? Sort.by(new ArrayList<>(orders)) : sort.and(Sort.by(new ArrayList<>()));
        return this;
    }

    public Query sort(@NonNull Direction direction, @NonNull QProperty... properties) {
        return sort(Sort.sort(direction, properties));
    }

    public Query asc(@NonNull QProperty... properties) {
        return sort(Direction.ASC, properties);
    }

    public Query desc(@NonNull QProperty... properties) {
        return sort(Direction.DESC, properties);
    }

    public Query limit(long offset, long limit) {
        this.limit = new LimitImpl(offset, limit);
        return this;
    }

    public Query limit(long limit) {
        this.limit = new LimitImpl(null, limit);
        return this;
    }

    @Override
    public Stream<Criterion> stream() {
        return criteria.stream();
    }


    @Override
    public void apply(@NonNull StringBuilder parent, @NonNull String value) {

        parent.append("<where>");

        for (int i = 0; i < this.criteria.size(); i++) {
            final Criterion criterion = this.criteria.get(i);
            parent.append("<trim prefix=\" AND \">");
            criterion.apply(parent, String.format("%s.criteria[%s]", value, i));
            parent.append("</trim>");
        }

        parent.append("</where>");

        if (Asserts.nonNull(this.sort)) {
            this.sort.apply(parent, String.format("%s.sort", value));
        }

        if (this.limit != null) {
            limit.apply(parent, String.format("%s.limit", value));
        }


    }
}
