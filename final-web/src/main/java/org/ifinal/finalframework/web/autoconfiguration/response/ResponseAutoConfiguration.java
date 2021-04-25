package org.ifinal.finalframework.web.autoconfiguration.response;

import java.util.Optional;
import org.ifinal.auto.spring.factory.annotation.SpringAutoConfiguration;
import org.ifinal.finalframework.web.response.advice.ResponsibleResponseBodyAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author likly
 * @version 1.0.0
 * @see ResponsibleResponseBodyAdvice
 * @since 1.0.0
 */
@Configuration
@SpringAutoConfiguration
@ConditionalOnClass(ResponsibleResponseBodyAdvice.class)
@EnableConfigurationProperties(ResponseProperties.class)
public class ResponseAutoConfiguration implements ApplicationContextAware {

    private final ResponseProperties properties;

    public ResponseAutoConfiguration(final ResponseProperties properties) {

        this.properties = properties;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {

        Optional.of(applicationContext.getBean(ResponsibleResponseBodyAdvice.class))
            .ifPresent(it -> it.setSyncStatus(properties.isSyncStatus()));
    }

}