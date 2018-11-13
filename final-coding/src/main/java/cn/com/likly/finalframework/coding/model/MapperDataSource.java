package cn.com.likly.finalframework.coding.model;

import cn.com.likly.finalframework.coding.annotation.Template;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0
 * @date 2018-11-08 10:16
 * @since 1.0
 */
@Template("mybatis/datasource.ftl")
public class MapperDataSource implements Serializable {
    private String packageName;
    private String name;
    private String[] basePackages;
    private String mapperLocations;
    private String prefix;
    private String dataSource;
    private String sqlSessionFactory;
    private String sqlSessionTemplate;

    public MapperDataSource(String packageName,
                            String name,
                            String[] basePackages,
                            String mapperLocations,
                            String prefix,
                            String dataSource,
                            String sqlSessionFactory,
                            String sqlSessionTemplate) {
        this.packageName = packageName;
        this.name = name;
        this.basePackages = basePackages;
        this.mapperLocations = mapperLocations;
        this.prefix = prefix;
        this.dataSource = dataSource;
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public String getPackage() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public String getBasePackages(){
        return Arrays.stream(basePackages).map(it -> String.format("\"%s\"", it)).collect(Collectors.joining(","));
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public String getSqlSessionTemplate() {
        return sqlSessionTemplate;
    }
}
