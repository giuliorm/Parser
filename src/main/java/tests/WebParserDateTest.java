package tests;

import ifmo.escience.newscrawler.parser.DateHandler;
import ifmo.escience.newscrawler.parser.DateTodayHandler;
import ifmo.escience.newscrawler.parser.DateYesterdayHandler;
import ifmo.escience.newscrawler.parser.WebPageParser;
import junit.framework.TestCase;
import org.junit.Test;

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

    WebPageParser parser = new WebPageParser();

    @Test
    public void testCheckWordsNormalFormat() {

        String date = "2016-12-01 03:10:11";
        String format = "yyyy-MM-dd hh:mm:ss";

        Date dateResult = null;
        try {
            Date expected = new SimpleDateFormat(format).parse("2016-12-01 03:10:11");
            dateResult = parser.dateFromString(date, format);
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

        Date result = new DateYesterdayHandler(date, format).handle();
        assertEquals(result, expected);
    }

    @Test
    public void testHandleYesterdayInvalidSymbols() {

        String date = "вче[][....ра, 9:40";
        LocalDateTime now = LocalDateTime.now();
        Date expected = Date.from(LocalDate.now().minusDays(1).atTime(9,40).atZone(ZoneId.systemDefault()).toInstant());

        String formatString = "dd.MM.yy hh:mm";
        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result = new DateYesterdayHandler(date, format).handle();
        assertEquals(result.compareTo(expected), 0);
    }

    @Test
    public void testHandleYesterdayWord() {

        String date = "вчера, 9:40";
        LocalDateTime now = LocalDateTime.now();
        Date expected = Date.from(LocalDate.now().minusDays(1).atTime(9,40).atZone(ZoneId.systemDefault()).toInstant());

        String formatString = "dd.MM.yy hh:mm";
        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result = new DateYesterdayHandler(date, format).handle();
        assertEquals(result.compareTo(expected), 0);
    }

    @Test
    public void testHandleTodayWord() {

        String date = "Сегодня, 7:30";
        Date expected = Date.from(LocalDate.now().atTime(7,30).atZone(ZoneId.systemDefault()).toInstant());

        String formatString = "dd.MM.yy hh:mm";
        SimpleDateFormat format = new SimpleDateFormat(formatString);

        Date result = new DateTodayHandler(date, format).handle();
        assertEquals(result.compareTo(expected), 0);
    }

    @Test
    public void testRemoveUnusedCharacters() {

        String date = "date ,,,s..+- [ ] ( )";
        String expected = "date s";
        date = DateHandler.removeUnnecessarySymbols(date);
        assertEquals(date.compareTo(expected), 0);

    }

}



