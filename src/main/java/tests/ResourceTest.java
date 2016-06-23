package tests;
import ifmo.escience.newscrawler.Utils;
import ifmo.escience.newscrawler.WebPage;
import ifmo.escience.newscrawler.WebPageParser;
import ifmo.escience.newscrawler.entities.WebEntity;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zotova on 22.06.2016.
 */

@RunWith(Parameterized.class)
public class ResourceTest extends TestCase {

    private static Map<String, WebEntity> existingEntities;
    static {
        existingEntities = Utils.getEntitiesList("multiConfig.json")
                .stream()
                .collect(Collectors.toMap(item -> Utils.getUrlStd(item.getEntityUrl()), item->item));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {}, {}
        });
    };

    private  String url;
    private String date;

    private WebPage page;
    private WebPageParser parser;
    private WebEntity resource;

    public ResourceTest(String url) {
        this.url = url;
        this.page = new WebPage(url);
        this.parser = new WebPageParser();
        parser.resetDriver(url);
        String key = Utils.getUrlStd(url);
        this.resource = existingEntities.containsKey(key) ? existingEntities.get(Utils.getUrlStd(url)) : null;
    }

    @Test
    public void testName() {

        if (resource != null) {
            WebElement element = parser.tryGetElement("ARTICLE HEADER", resource.getArticleNamePath());
            if (element == null)
                fail();

            parser.parseHeader(page, element);

            String parsedName = page.getArticleName();

            if (parsedName == null)
                fail();
            if (parsedName.isEmpty())
                fail();

        }

    }

    @Test
    public void testText() {



    }


    @Test
    public void parseDateTest() {



    }

    @Test
    public void checkWordsTest() {



    }

    @Test
    public void RegExpMatcherTest() {

        if (date == null)
            fail();
    }

}


