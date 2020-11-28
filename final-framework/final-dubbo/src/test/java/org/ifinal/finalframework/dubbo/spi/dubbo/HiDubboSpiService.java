package org.ifinal.finalframework.dubbo.spi.dubbo;


import org.ifinal.finalframework.auto.service.annotation.AutoService;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@AutoService(value = DubboSpiService.class, name = "hi")
public class HiDubboSpiService implements DubboSpiService {
    @Override
    public String helloSpi() {
        return "HiSpiService";
    }
}
