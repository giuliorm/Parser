package ifmo.escience.newscrawler.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zotova on 24.06.2016.
 */
public abstract class DateHandler {

    protected String date;
    protected  DateHandler next;
    protected  DateFormat format;

    public DateHandler(String date, DateFormat format) {
        this.date = date;
        this.format = format;
    }

    public static String  removeUnnecessarySymbols(String date) {
        return date.replaceAll(",*\\+*\\-*\\.*\\[*\\]*\\(*\\)*", "").trim();

    }

    protected String preHandleDate(String date){
        return removeUnnecessarySymbols(date).toLowerCase();
    }

    public void setNext(DateHandler next) {
        this.next = next;
    }

    public DateHandler getNext() {
        return next;
    }

    public abstract Date handle();

    public boolean hasNext() {
        return this.next != null;
    }

    protected Date tryGetDateFromString(String date) {

        try  {
            return format.parse(date);
        }
        catch (ParseException e) {

        }
        catch (Exception e) {

        }
        return null;
    }

}
