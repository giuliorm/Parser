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
                {  "url", "name", "text", "date"  },
        });
    };

    private  String url;
    private String expectedName;
    private String expectedText;
    private String expectedDate;
    private HtmlUnitDriver driver;

    public ResourceTest(String url, String expectedName, String expectedText, String expectedDate) {
        this.driver = new HtmlUnitDriver();
        this.url = url;
        this.expectedName = expectedName;
        this.expectedText = expectedText;
        this.expectedDate = expectedDate;
    }

    @Test
    public void testName() {
        WebPage page = new WebPage(url);
        WebPageParser parser = new WebPageParser();
        WebEntity resource = existingEntities.get(Utils.getUrlStd(url));

        parser.resetDriver(url);
        parser.parseHeader(page, parser.tryGetElement("ARTICLE HEADER", resource.getArticleNamePath()));
        assertEquals(expectedName, page.getArticleName());

    }

    @Test
    public void testDate() {
    }

    @Test
    public void testText() {



    }
}


