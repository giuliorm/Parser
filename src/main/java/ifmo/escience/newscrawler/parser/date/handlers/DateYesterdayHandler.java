package ifmo.escience.newscrawler.parser.date.handlers;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zotova on 24.06.2016.
 */
public class DateYesterdayHandler extends DateStringDateHandler {

    Pattern yesterdayPattern = Pattern.compile("(вчера)");

    public DateYesterdayHandler(String date, DateFormat format, DateStringHandler... preHandlers) {
        super(date, format, preHandlers);
    }

    @Override
    protected Date handle(String date) {
        LocalDateTime now = LocalDateTime.now();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        String handledDate = handleDate();
        String year = String.valueOf(now.getYear());
        String day =  String.valueOf(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
        String month = String.valueOf(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        String dateString = day + "." + month + "." + year;

        Matcher patternMatcher = yesterdayPattern.matcher(handledDate);
        String newDate = patternMatcher.replaceFirst(dateString);

        return tryGetDateFromString(newDate);
    }
}
