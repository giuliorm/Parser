package tests;

import ifmo.escience.newscrawler.Utils;
import ifmo.escience.newscrawler.WebPage;
import ifmo.escience.newscrawler.WebPageParser;
import ifmo.escience.newscrawler.entities.WebEntity;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import static org.junit.Assert.fail;

/**
 * Created by zotova on 23.06.2016.
 */
@RunWith(Parameterized.class)
public class WebParserDateTest extends TestCase {

    String date;
    String pattern;
    String format;
    String expected;

    WebPageParser parser = new WebPageParser();


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            /*    { null,  null, null  },
                { null, null, ""},
                { null, "", null },
                { null, "", "" },
                { "", null, null },
                { "", null, ""},
                { "", "", null },
                { "", "" , ""}, */
                { "2016-12-01 03:10:11", "", "yyyy-MM-dd hh:mm:ss", "2016-12-01 03:10:11" },
                { "вчера, 9:40", "\\d{2}:\\d{2}", "dd.MM.yy hh:mm", "22.06.2016 9:40" } ,
                { "Сегодня, 9:40", "\\d{2}:\\d{2}", "dd.MM.yy hh:mm", Calendar.getInstance().toString() + " 9:40" },
                { "2016-12-01 03:10:11", "", "yyyy MM dd hh:mm:ss", "01.12.2016 03:10:11" },
                { "29 июня 2016" , "", "dd MMMM yyyy hh:mm", "29.06.2016"},
                { "29 июня 2016 22:22" , "", "dd MMMM yyyy hh:mm", "29.06.2016 22:22" }
        });
    };

    public WebParserDateTest(String date, String pattern, String format, String expected) {
        this.date = date;
        this.pattern = pattern;
        this.format = format;
        this.expected = expected;
    }


    @Test
    public void checkWordsTest() {

        Date dateResult = null;
        try {
             dateResult = parser.checkWords(date, format);
            Date expectedDate = new SimpleDateFormat(format).parse(expected);
            assertEquals(dateResult.compareTo(expectedDate), 0);
        }
        catch(ParseException e) {
            fail();
        }

    }

    @Test
    @Ignore
    public void RegExpMatcherTest() {
        String match = parser.dateRegexpMatch(date, pattern);
        System.out.println(match);
    }
}






