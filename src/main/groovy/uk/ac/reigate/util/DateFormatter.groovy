package uk.ac.reigate.util

import java.text.ParseException
import java.text.SimpleDateFormat

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.format.Formatter
import org.springframework.stereotype.Component

class DateFormatter implements Formatter<Date> {
    
    private final String DEFAULT_DATE_FORMAT = 'dd/MM/yyyy'
    
    public DateFormatter() {
        super();
    }
    
    public Date parse(final String text, final Locale locale) throws ParseException {
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.parse(text);
    }
    
    public String print(final Date object, final Locale locale) {
        final SimpleDateFormat dateFormat = createDateFormat(locale);
        return dateFormat.format(object);
    }
    
    private SimpleDateFormat createDateFormat(final Locale locale) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        dateFormat.setLenient(false);
        return dateFormat;
    }
}
