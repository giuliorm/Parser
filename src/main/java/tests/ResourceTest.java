package tests;
import ifmo.escience.newscrawler.helpers.Utils;
import ifmo.escience.newscrawler.WebPage;
import ifmo.escience.newscrawler.parser.WebPageParser;
import ifmo.escience.newscrawler.entities.WebEntity;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebElement;

import java.util.*;
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
    public static Collection<Object> data() {
        return Arrays.asList(new Object[] {
          //   "http://rustelegraph.ru/news/2016-06-26/Vypuskniki-v-sostoyanii-slegka-pod-parusom-60576/",
           //  "http://47news.ru/articles/105203/",
          //  "http://piter.tv/event/Napadayuschij_Sankt_Peterburgskogo_Zenita_otmetil_s_zhenoj_sitcevuyu_svad_bu/",
                "http://spbvoditel.ru/2016/06/27/042/",
        });
    };

    private  String url;

    private WebPage page;
    private WebPageParser parser;
    private WebEntity resource;

    public ResourceTest(String url) {
        this.url = url;
        this.page = new WebPage(url);
        this.parser = new WebPageParser();
        String key = Utils.getUrlStd(url);
        this.resource = existingEntities.containsKey(key) ? existingEntities.get(Utils.getUrlStd(url)) : null;

        System.out.println("---------------Entity: " + url + "-----------------");
    }

    @Test
    public void testName() throws InterruptedException {

        if (resource != null) {
            Thread.sleep(2_000);
            parser.resetDriver(url);
            WebElement element = parser.tryGetElement("ARTICLE HEADER", resource.getArticleNamePath());
            if (element == null)
                fail();

            parser.parseHeader(page, element);

            String parsedName = page.getArticleName();

            if (parsedName == null)
                fail();
            if (parsedName.isEmpty())
                fail();
            System.out.println("Article name: " + parsedName);
        }

    }


    @Test
    public void testDate()throws InterruptedException {

        if (resource != null) {
            Thread.sleep(2_000);
            parser.resetDriver(url);
            WebElement element = parser.tryGetElement("ARTICLE DATE", resource.getArticleDatePath());
            if (element == null)
                fail();

            parser.parseDate(page, resource, element);

            Date date = page.getArticleDate();

            if (date == null)
                fail();
            System.out.println("Article Date: " + date);
        }

    }

    @Test
    public void testText() throws InterruptedException {

        if (resource != null) {
            Thread.sleep(2_000);
            parser.resetDriver(url);
            List<WebElement> elements = parser.tryGetElements("ARTICLE TEXT", resource.getArticleTextPath());
            if (elements == null || elements != null && elements.size() < 1)
                fail();

            parser.parseBody(page, elements);

            String text = page.getArticleText();

            if (text == null || text != null && text.isEmpty())
                fail();
          //  System.out.println("Article text: " + text);
        }
    }
}


