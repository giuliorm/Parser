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
        parser = new WebPageParser();
    }

    @Parameterized.Parameters
    public static Collection<Object> data() {
        return Arrays.asList(new Object[] {
         /*    "http://rustelegraph.ru/news/2016-06-26/Vypuskniki-v-sostoyanii-slegka-pod-parusom-60576/",
             "http://47news.ru/articles/105203/",
            "http://piter.tv/event/Napadayuschij_Sankt_Peterburgskogo_Zenita_otmetil_s_zhenoj_sitcevuyu_svad_bu/",
               "http://spbvoditel.ru/2016/06/27/042/", */
          //      "https://saint-petersburg.ru/m/society/apaley/350108/"
        //    "https://life.ru/t/life78/873799/pietierburzhtsy_moghut_kupit_biliety_na_kubok_konfiedieratsii_po_futbolu_za_960_rubliei"
      //  "http://online47.ru/2016/07/05/Vo-Vsevolozhskii-i-Tosnenskii-raiony-vernulsya-svet-32562"
               //"http://ria.ru/spb/20140320/1000398165.html"
            //    "http://www.spb.aif.ru/society/people/bolee_25_tysyach_musulman_otprazdnovali_uraza-bayram_v_peterburge"
          //  "http://www.fontanka.ru/2016/06/27/140/"
             //!CANNOT PARSE AND DONT KNOW HOW
                // -"http://konkretno.ru/2016/06/27/zamdirektora-volosovskogo-psixinternata-ukrala-u-pacientov-13-milliona-rublej.html"
         //   "http://47channel.ru/event/Pravitel_stvo_pereraspredelilo_subsidii_chtobi_dopolnitel_no_podderzhat_malij_biznes_v_monogorodah/"
        //"http://www.spbdnevnik.ru/news/2016-07-05/vtoraya-smena-foruma-vsmysle-perenositsya-iz-za-nepogody/"
       // "http://vyborg.tv/obshchestvo/26442-kak-vychislit-lzhekontroljorov.html"
           //     "http://neva.today/news/127426/"
      //"https://baltika.fm/news/97187"
      //  "http://ok-inform.ru/obshchestvo/proisshestviya/66098-v-tatarstane-11-letnego-malchika-na-glazakh-u-roditelej-nasmert-zasosalo-v-trubu-nasosnoj-stantsii-na-ozere.html"
     //   "http://www.interfax-russia.ru/view.asp?id=742044"
      //  "http://gatchina24.ru/news/2016/07/05/news_17238.html"
     //  "http://www.ntv.ru/novosti/1640876/"
     // "http://topspb.tv/news/news108075/"
            //    "http://www.vesti.ru/doc.html?id=2773418"
      //  "http://gatchina-news.ru/news/incident/gatchinskij-pedofil-osuzhden-na-17-let-strogogo-rezhima-9896.html"
       // "https://regnum.ru/news/polit/2153010.html"
              //  "http://mr7.ru/articles/135346/"
     //  "http://www.rosbalt.ru/world/2016/07/06/1529771.html"
    //  "http://lenobl.ru/news22352.html"
       // "http://www.gazeta.spb.ru/1963215-0/"
     //   "http://sledcomrf.ru/news/244944-sostoyalas-vstrecha-s-predstavitelyami.html"
           ///     "http://mr7.ru/articles/135627/",
             //   "https://78.mvd.ru/news/item/8123022/",
             //   "http://www.spb.kp.ru/daily/26553/3570028/",
             //   "http://lenoblinform.ru/news/Rostelekom-Vigodnoe-leto-110716.html"
           //     "http://www.spb.aif.ru/incidents/chp/v_lenoblasti_devochka_sorvalas_s_osvetitelnoy_bashni_vo_vremya_selfi",
                "http://www.spb.kp.ru/daily/26554/3570327/"
        });
    };

    private  String url;

    private WebPage page;
    private static WebPageParser parser;
    private WebEntity resource;

    public ResourceTest(String url) {
        this.url = url;
        this.page = new WebPage(url);
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


