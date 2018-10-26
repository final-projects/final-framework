package cn.com.likly.finalframework.util;

import java.util.stream.Stream;

/**
 * @author likly
 * @version 1.0
 * @date 2018-10-25 10:31
 * @since 1.0
 */
public interface Streable<T> {
    Stream<T> stream();
}
