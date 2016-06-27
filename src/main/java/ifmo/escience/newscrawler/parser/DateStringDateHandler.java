package ifmo.escience.newscrawler.parser;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by JuriaSan on 27.06.2016.
 */
public abstract class DateStringDateHandler extends DateHandler<String, Date> {

    public DateStringDateHandler(String date, DateFormat format, DateHandler... preHandlers) {
        super(date, format, preHandlers);
    }

    public Date handle() {
        return handle(date);
    }
}
