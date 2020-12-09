package org.ifinal.finalframework.boot.autoconfigure.sharding;


import org.ifinal.finalframework.sharding.config.ShardingConfigurer;
import org.ifinal.finalframework.sharding.config.ShardingDataSourceRegistry;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(ShardingDataSourceProperties.class)
public class ShardingDataSourceConfigurer implements ShardingConfigurer {

    private static final String SPRING_DATA_SOURCE_PREFIX = "spring.datasource";
    private static final String DEFAULT_DATASOURCE_NAME = "ds";


    private final ShardingDataSourceProperties properties;
    private final DataSourceProperties dataSourceProperties;

    public ShardingDataSourceConfigurer(ShardingDataSourceProperties properties, DataSourceProperties dataSourceProperties) {
        this.properties = properties;
        this.dataSourceProperties = dataSourceProperties;
    }

    @Override
    public void addDataSource(@NonNull ShardingDataSourceRegistry registry) {

        if (CollectionUtils.isEmpty(properties.getDatasource())) {
            registry.addDataSource(DEFAULT_DATASOURCE_NAME, dataSourceProperties.initializeDataSourceBuilder().build());
        } else {
            for (Map.Entry<String, DataSourceProperties> entry : properties.getDatasource().entrySet()) {
                DataSourceProperties dataSourceProperties = entry.getValue();
                registry.addDataSource(entry.getKey(), dataSourceProperties.initializeDataSourceBuilder().build());
            }
        }

    }

//    @Bean
//    @ConfigurationProperties(SPRING_DATA_SOURCE_PREFIX)
//    public DataSourceProperties springDataSource() {
//        return new DataSourceProperties();
//    }


}

