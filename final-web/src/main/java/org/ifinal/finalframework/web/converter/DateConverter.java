package org.ifinal.finalframework.web.converter;

import java.util.Date;
import org.ifinal.finalframework.util.format.DateFormatters;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author likly
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(final String source) {

        return DateFormatters.DEFAULT.parse(source);
    }

}