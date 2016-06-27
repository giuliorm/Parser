package ifmo.escience.newscrawler.parser;

import ifmo.escience.newscrawler.helpers.LocaleHolder;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zotova on 24.06.2016.
 */
public class DateTodayHandler extends  DateHandler {

    Pattern ruTodayPattern = Pattern.compile("(сегодня)");
    public DateTodayHandler(String date, DateFormat format) {
        super(date, format);
    }

    @Override
    public Date handle() {
        LocalDateTime now = LocalDateTime.now();

        String handledDate = preHandleDate(date);
        String year = String.valueOf(now.getYear());
        String day = String.valueOf(String.format("%02d", now.getDayOfMonth()));
        String month = String.valueOf(String.format("%02d", now.getMonthValue()));
        String dateString = day + "." + month + "." + year;

        Matcher patternMatcher = ruTodayPattern.matcher(handledDate);
        String newDate = patternMatcher.replaceFirst(dateString);

        return tryGetDateFromString(newDate);
    }
}