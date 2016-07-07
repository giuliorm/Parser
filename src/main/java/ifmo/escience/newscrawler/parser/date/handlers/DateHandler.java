package ifmo.escience.newscrawler.parser.date.handlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zotova on 24.06.2016.
 */
public abstract class DateHandler<TArgument, TResult> {

    List<DateHandler<TArgument, TArgument>> preHandlers;
    protected TArgument date;
    protected  DateFormat format;

    public DateHandler(TArgument date, DateFormat format, DateHandler... preHandlers) {
        this.preHandlers = preHandlers != null ? Arrays.asList(preHandlers) : null;
        this.date = date;
        this.format = format;
    }


    protected TArgument handleDate() {
        TArgument handledDate = date;
        if (preHandlers != null && preHandlers.size() > 0) {
            for(DateHandler h : preHandlers) {
                try {
                    handledDate = (TArgument) h.handle(handledDate);
                }
                catch(ClassCastException ex) {
                    handledDate = null;
                }
            }
        }
        return handledDate;
    }

    public DateHandler() {

    }

    protected abstract TResult handle(TArgument date);

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
