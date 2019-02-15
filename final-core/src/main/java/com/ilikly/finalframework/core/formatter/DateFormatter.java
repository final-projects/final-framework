package com.ilikly.finalframework.core.formatter;

import com.ilikly.finalframework.core.Assert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author likly
 * @version 1.0
 * @date 2019-02-15 21:48:24
 * @since 1.0
 */
@SuppressWarnings("all")
public class DateFormatter implements Formatter<Date> {

    public static final DateFormatter YYYY_MM_DD_HH_MM_SS = new DateFormatter(DateFormatterPattern.YYYY_MM_DD_HH_MM_SS);
    public static final DateFormatter YYYY_MM_DD = new DateFormatter(DateFormatterPattern.YYYY_MM_DD);
    public static final DateFormatter YYYY__MM__DD_HH_MM_SS = new DateFormatter(DateFormatterPattern.YYYY__MM__DD_HH_MM_SS);
    public static final DateFormatter YYYY__MM__DD = new DateFormatter(DateFormatterPattern.YYYY__MM__DD);
    public static final DateFormatter YYYYMMDD_HH_MM_SS = new DateFormatter(DateFormatterPattern.YYYYMMDD_HH_MM_SS);
    public static final DateFormatter YYYYMMDD = new DateFormatter(DateFormatterPattern.YYYYMMDD);
    public static final DateFormatter YYYYMMDDHHMMSS = new DateFormatter(DateFormatterPattern.YYYYMMDDHHMMSS);

    private final String regex;
    private final DateFormat dateFormat;

    public DateFormatter(String regex, String pattern) {
        this.regex = regex;
        this.dateFormat = new SimpleDateFormat(pattern);
    }

    public DateFormatter(DateFormatterPattern pattern) {
        this(pattern.getRegex(), pattern.getPattern());
    }

    @Override
    public Date parse(String source) {
        try {
            return Assert.isEmpty(source) ? null : dateFormat.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String format(Date target) {
        return dateFormat.format(target);
    }

    @Override
    public boolean matches(String source) {
        return source.matches(regex);
    }
}
