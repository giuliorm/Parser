package ifmo.escience.newscrawler.parser.date.handlers;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by zotova on 24.06.2016.
 */
public class DateFromFormatHandler extends DateStringDateHandler {


    public DateFromFormatHandler (String date, DateFormat format, DateStringHandler... preHandlers) {
        super (date, format, preHandlers);

    }

    @Override
    protected Date handle(String date) {

        String handledDate = handleDate();

        return tryGetDateFromString(handledDate);
    }
}
