package org.finalframework.spring.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.finalframework.spring.coding.ApplicationEventListener;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author likly
 * @version 1.0
 * @date 2019-01-09 21:18:06
 * @since 1.0
 */
@Slf4j
@ApplicationEventListener
public class ApplicationStartingEventListener implements ApplicationListener<ApplicationStartingEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        System.out.println(event.getClass().getCanonicalName());
    }
}
