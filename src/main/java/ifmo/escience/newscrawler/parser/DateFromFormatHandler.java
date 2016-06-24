package ifmo.escience.newscrawler.parser;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by zotova on 24.06.2016.
 */
public class DateFromFormatHandler extends DateHandler {

    boolean needsPreHandling;
    public DateFromFormatHandler (String date, DateFormat format, boolean needsPreHandling) {
        super (date, format);
        this.needsPreHandling = needsPreHandling;
    }

    @Override
    public Date handle() {
        String handleDate = needsPreHandling ? preHandleDate(date) : date;
        return tryGetDateFromString(handleDate);
    }
}
