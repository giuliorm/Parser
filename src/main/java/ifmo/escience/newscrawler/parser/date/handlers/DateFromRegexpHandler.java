package ifmo.escience.newscrawler.parser.date.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JuriaSan on 27.06.2016.
 */
public class DateFromRegexpHandler  extends DateStringHandler {

    Pattern pattern;

    public DateFromRegexpHandler(String pattern) {
        super();
        if (pattern == null)
            pattern = "";

        this.pattern = Pattern.compile(pattern);
    }
    @Override
    public String handle(String date) {
        Matcher matcher = pattern.matcher(date);
        while(matcher.find()) {
            String g = matcher.group();
            return g;
        }
        return null;
    }

}
