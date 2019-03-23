package org.finalframework.spring.web.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.finalframework.spring.web.reponse.DefaultResponseInterceptor;
import org.finalframework.spring.web.reponse.ResponseInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author likly
 * @version 1.0
 * @date 2019-02-14 11:04:03
 * @since 1.0
 */
@ConfigurationProperties(prefix = ResponseBodyAdviceProperties.RESPONSE_PROPERTIES)
@Setter
@Getter
public class ResponseBodyAdviceProperties {
    static final String RESPONSE_PROPERTIES = "final.response";
    /**
     * 是否同步业务状态到Response中
     */
    private boolean syncStatus = true;
    private Class<? extends ResponseInterceptor> interceptor = DefaultResponseInterceptor.class;


}