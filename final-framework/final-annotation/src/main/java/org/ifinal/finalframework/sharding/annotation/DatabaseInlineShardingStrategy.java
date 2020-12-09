package org.ifinal.finalframework.sharding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author likly
 * @version 1.0.0
 * @see org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration
 * @see org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration
 * @see org.apache.shardingsphere.sharding.algorithm.sharding.inline.InlineShardingAlgorithm
 * @since 1.0.0
 */
@ShardingStrategy(scope = ShardingScope.DATABASE, type = ShardingType.INLINE)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseInlineShardingStrategy {

    String name() default "";

    String[] columns();

    @Property("algorithm-expression")
    String expression();

}
