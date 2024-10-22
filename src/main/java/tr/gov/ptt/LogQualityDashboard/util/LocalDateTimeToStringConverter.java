package tr.gov.ptt.LogQualityDashboard.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public String convert(LocalDateTime source) {
        return source.format(formatter);
    }
}