package tests;

import ifmo.escience.newscrawler.entities.WebEntity;
import ifmo.escience.newscrawler.parser.*;
import ifmo.escience.newscrawler.parser.date.handlers.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.Assert.fail;

/**
 * Created by zotova on 23.06.2016.
 */
public class WebParserDateTest extends TestCase {

    DateStringSymbolsHandler symbols ;
    DateStringSimpleHandler simple;
    WebPageParser parser = new WebPageParser();


    public WebParserDateTest() {
        symbols = new DateStringSymbolsHandler();
        simple =  new DateStringSimpleHandler();
    }
    @Test
    public void testCheckWordsNormalFormat() {

        String date = "2016-12-01 03:10:11";
        String format = "yyyy-MM-dd hh:mm:ss";

        Date dateResult = null;
        WebEntity entity = new WebEntity(null, null, null);
        entity.setDateFormat(format);
        entity.setRegExpForDate("\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}");
        try {
            Date expected = new SimpleDateFormat(format).parse("2016-12-01 03:10:11");

            dateResult = parser.dateFromString(date, entity);
            assertEquals(dateResult.compareTo(expected), 0);
        }
        catch(ParseException e) {
            fail();
        }

    }

    @Test
    public void testHandleInvalidYesterdayWord() {

        String date = "вче[]sss[ра, 9:40";
        LocalDateTime now = LocalDateTime.now();
        Date expected = null;
        String formatString = "dd.MM.yy hh:mm";
        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result = new DateYesterdayHandler(date, format, simple, symbols).handle();
        assertEquals(result, expected);
    }


    @Test
    public void testDateFromRegexp() {
        String pattern = "\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}";
        String date = "ывроыро \\\\ ололоо! 27.06.2016 12:15";
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        Date expected = null;
        Date result = null;
        try {
             expected = format.parse("27.06.2016 12:15");
             result = format.parse(new DateFromRegexpHandler(pattern).handle(date));
        }
        catch(ParseException ex) {
            fail();
        }

        assertEquals(expected.compareTo(result), 0);
    }

    @Test
    public void testHandleYesterdayInvalidSymbols() {

        String date = "вче[][....ра, 9:40";
        LocalDateTime now = LocalDateTime.now();
        Date expected = Date.from(LocalDate.now().minusDays(1).atTime(9,40).atZone(ZoneId.systemDefault()).toInstant());

        String formatString = "dd.MM.yy hh:mm";
        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result = new DateYesterdayHandler(date, format, simple, symbols).handle();
        assertEquals(result.compareTo(expected), 0);
    }


    @Test
    public void testTimeFormat() {

        String ds = "17:25";
        String timeFormatString = "hh:mm";
        String dateformatString = "hh:mm, dd MMMM";
        Date result = null;
        WebEntity entity = new WebEntity(null, null, null);
        entity.setDateFormat(dateformatString);
        entity.setRegExpForDate("\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}");
        entity.setTimeFormat(timeFormatString);

        try
        {
            Date exp = new SimpleDateFormat(timeFormatString).parse(ds);
            result = parser.dateFromString(ds, entity);
            assertEquals(exp.compareTo(result), 0);
        }
        catch (ParseException e) {

        }
        ;
    }

    @Test
    public void testHandleYesterdayWord() {

        String date = "вчера, 9:40";
        Date expected = Date.from(LocalDate.now().minusDays(1).atTime(9,40).atZone(ZoneId.systemDefault()).toInstant());

        String formatString = "dd.MM.yy hh:mm";
        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result = new DateYesterdayHandler(date, format, simple, symbols).handle();
        assertEquals(expected.compareTo(result), 0);
    }

    @Test
    public void testHandleTodayWord() {

        String date = "Сегодня, 7:30";
        Date expected = Date.from(LocalDate.now().atTime(7,30).atZone(ZoneId.systemDefault()).toInstant());

        String formatString = "dd.MM.yy hh:mm";
        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result = new DateTodayHandler(date, format, simple, symbols).handle();
        assertEquals(expected.compareTo(result), 0);
    }


    @Test
    public void testRemoveUnusedCharacters() {

        String date = "date ,,,s..+- [ ] ( )";
        String expected = "date s";
        date = simple.handle(symbols.handle(date));
        assertEquals(date.compareTo(expected), 0);

    }

}



