package cn.com.likly.finalframework.coding.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author likly
 * @version 1.0
 * @date 2018-10-19 20:38
 * @since 1.0
 */
public class EntityModel {
    private String entityPackage;
    private String entityName;
    private String holderPackage;
    private String holderName;
    private String mapperPackage;
    private String mapperName;
    private String repositoryName;
    private String repositoryPackage;
    private Set<PropertyModel> properties;
    private PropertyModel idProperty;
    private boolean mapperEntity;


    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getHolderPackage() {
        return holderPackage;
    }

    public void setHolderPackage(String holderPackage) {
        this.holderPackage = holderPackage;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryPackage() {
        return repositoryPackage;
    }

    public void setRepositoryPackage(String repositoryPackage) {
        this.repositoryPackage = repositoryPackage;
    }

    public Set<PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(Set<PropertyModel> properties) {
        this.properties = properties;

        List<PropertyModel> idProperties = properties.stream().filter(PropertyModel::isIdProperty).collect(Collectors.toList());
        if (idProperties.size() == 1) {
            this.idProperty = idProperties.get(0);
        }

    }

    public PropertyModel getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(PropertyModel idProperty) {
        this.idProperty = idProperty;
    }

    public boolean isMapperEntity() {
        return mapperEntity;
    }

    public void setMapperEntity(boolean mapperEntity) {
        this.mapperEntity = mapperEntity;
    }
}