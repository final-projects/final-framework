package org.ifinal.finalframework.context.resource;

import java.util.Arrays;
import java.util.Collection;
import org.ifinal.finalframework.context.annotation.ResourceValue;
import org.ifinal.finalframework.json.Json;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * BeanFactoryResourceValueManager.
 *
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class BeanFactoryResourceValueManager implements ResourceValueManager, ApplicationContextAware {

    private final MultiValueMap<String, ResourceValueHolder> cache = new LinkedMultiValueMap<>(256);

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        Arrays.stream(applicationContext.getBeanNamesForAnnotation(ResourceValue.class))
            .map(applicationContext::getBean)
            .forEach(bean -> {
                Class<?> targetClass = AopUtils.getTargetClass(bean);

                for (final ResourceValueHolder holder : ResourceValueUtils.findAllResourceValueHolders(bean, targetClass)) {
                    addResourceValueHolder(holder.getKey(), holder);
                }

            });
    }

    @Override
    public Collection<ResourceValueHolder> getResourceValueHolders(final String key) {
        return cache.get(key);
    }

    @Override
    public void addResourceValueHolder(final String key, final ResourceValueHolder holder) {
        cache.add(key, holder);
    }

    @Override
    public void setValue(final String key, final String value) {
        cache.get(key).forEach(holder -> holder.setValue(Json.toObject(value, holder.getValueType())));
    }

}