package ifmo.escience.newscrawler;

import ifmo.escience.newscrawler.database.NewsMongoDb;
import ifmo.escience.newscrawler.entities.RootEntity;
import ifmo.escience.newscrawler.entities.WebEntity;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.*;

public class WebPageParser {
    HtmlUnitDriver driver;

    private static Logger logger = LogManager.getLogger(WebPageParser.class.getName());

    private static final String ARTICLE_NAME = "ARTICLE NAME";
    private static final String ARTICLE_TEXT = "ARTICLE TEXT";
    private static final String TAGS = "TAGS";
    private static final String SIMILAR_NEWS = "SIMILAR NEWS";
    private static final String DATE = "DATE";
   // NewsMongoDb dbConnection;
   // private List<String> webLinks;
    //private WebEntity entity;

    static {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    }

    public WebPageParser() { //, List<String> arrayOfWebPage

        //this.webLinks = arrayOfWebPage;
        //this.entity = entity;
        driver = new HtmlUnitDriver();
        //dbConnection = connection;
//        Proxy proxy = new Proxy();
//        proxy.setHttpProxy("proxy.ifmo.ru:3128");
//        driver.setProxySettings(proxy);
    }

  //  public NewsMongoDb getDbConnection() {
     //   return this.dbConnection;
   // }

    //public void addPage(String page) {
     //   webLinks.add(page);
    //}
    public void resetDriver(String link) {
        driver.get(link);
    }

   // public HtmlUnitDriver getDriver() {
   //     return driver;
  //  }

  /*  public void parse(List<String> links, NewsMongoDb connection) {

        for(String link : links) {
            try {

               // HashMap<String, WebEntity> existingEntities = entity.getExistingEntities();
              //  String key = getUrlStd(link);
              //  if (existingEntities.containsKey(key)) {
                    parsePage(link, connection, entity);
              //  }
            }
            catch(Exception e) {
                logger.error("Cannot parse the link  " + link + " exception " + e.getMessage());
            }
        }  */

        /*while (webLinks.size() != 0) {
            try {
                parsePage(webLinks.get(0));//......
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Web page is not starting " + webLinks.get(0));
            }
            webLinks.remove(0);
        } */
    //}
    public boolean parseHeader(WebPage page, WebElement header) {
        if (header == null)
            return false;

        String headerString = header.getText();
        if (headerString == null || headerString.isEmpty())
            System.out.println("Header is null or empty for entity " + page.getPageUrl());

        page.setArticleName(header.getText());
        return true;

    }

    public boolean parseBody(WebPage page, List<WebElement> bodyElement) {

        if (bodyElement == null)
            return false;

        StringBuilder body = new StringBuilder();
            for (WebElement element : bodyElement) {
                body.append(element.getText());
            }

        if (body.length() < 1)
            System.out.println("Body is empty for entity " + page.getPageUrl());

        page.setArticleText(body.toString());
        return true;
    }

    public boolean parseSimilarLinks(WebPage page, WebEntity entity, List<WebElement> onlyLinks) {

        List<String> similarLinks = new LinkedList<String>();

        if (!entity.getSimilarNewsPath().isEmpty() && onlyLinks.size() > 0) {

            for (WebElement similarLink : onlyLinks) {
                similarLinks.add(similarLink.getAttribute("href"));
            }
            page.setSimilarNews(similarLinks.toString());
            return true;
        }

        return false;

    }

    public boolean parseTags(WebPage page, WebEntity entity, List<WebElement> tagsElement) {
        String tags = null;
        if (!entity.getTagsPath().isEmpty() && tagsElement.size() > 0) {

            List<String> tagsList = new LinkedList<String>();
            for (WebElement element : tagsElement) {
                tagsList.add(element.getText());
            }
            tags = tagsList.toString();
            page.setTags(tags);
            return true;
        }
        return false;
    }

    public boolean checkDate(WebEntity entity, String dateString) {

        if (dateString == null)
            return false;

        List<DateFormat> formats = new LinkedList<DateFormat>();
        formats.add(new SimpleDateFormat("dd.mm.yyyy"));

        for (DateFormat format : formats) {
            try {
                format.parse(dateString);
            }
            catch(ParseException ex) {
          /*      System.out.println("Date " + dateString + " DOES NOT COMPLY to pattern " + format.toString() + " of entity "
                       + entity != null ? entity.getEntityUrl() : "" +
                       " in thread #" + Thread.currentThread().getName()); */
                return false;
            }
        }
       /* System.out.println("Date " + dateString + "  COMPLIES to all patterns in thread #" +
                Thread.currentThread().getName() + " of entity "
                + (entity != null ? entity.getEntityUrl() : "")); */
        return true;

    }

    public boolean parseDate(WebPage page, WebEntity entity, WebElement date) {

        String articleDate = null;
        if (date == null)
            return false;

        try {

             System.out.println("Date " + date.getText() + " for url " + page.getPageUrl());

             articleDate = checkWords(dateRegexpMatch(date.getText().replaceAll("\n", " "),
                             entity.getRegExpForDate()),
                     entity.getDateFormat());
        }
        catch(ParseException pe) {
            logger.error("Parse exception at " + pe.getMessage());
            return false;
        }
        //System.out.println(articleDate + " " + newPage.getPageUrl() + "\n" +Header + "\n" + Body
        //       + "\n" + Tags + similarLinks );
        if (!checkDate(entity, articleDate))
        {
            System.out.println("Date " + articleDate + " is NOT COMPLY to suitable format");
            return false;
        }

        page.setArticleDate(articleDate);

        return true;
        //System.out.print("Parsed date " + articleDate + " for url " + entity.getEntityUrl());
    }

    public WebElement tryGetElement(String elementName, String path) {
        WebElement element = null;

        if (path != null && !path.isEmpty()) {
            try {
                element = driver.findElement(By.xpath(path));
            }
            catch(Exception e) {
                System.out.println("Unable to get " + elementName +
                        " from url " + driver.getCurrentUrl() + " by path " + path);
                String body = driver.findElement(By.tagName("body")).getText();
                //logger.error(e.getMessage());
            }
        }
        return element;
    }

    public List<WebElement> tryGetElements(String elementName, String path) {
        List<WebElement> elements = new LinkedList<>();

        if (path != null && !path.isEmpty()) {
            try {
                elements = driver.findElements(By.xpath(path));
            }
            catch(Exception e) {
                System.out.println("Unable to get " + elementName +
                        " from url " + driver.getCurrentUrl() + " by path " + path);
                logger.error(e.getMessage());
            }
        }

        return elements;
    }
    public void parsePage(WebEntity entity, NewsMongoDb dbConnection) throws Exception {

        if (!dbConnection.urlExists(entity.getEntityUrl())) {

            WebPage newPage = new WebPage(entity.getEntityUrl());
            newPage.setEntityUrl(Utils.getUrlStd(entity.getEntityUrl()));
            // WebEntity coreEntity = existingEntities.get(key);
            newPage.setParseTime(System.nanoTime());

            if (newPage.getPageUrl() == null || newPage.getPageUrl().isEmpty()) {
                System.out.println("ACHTUNG! ACHTUNG!");
            }

            //driver.get(newPage.getPageUrl());
            resetDriver(newPage.getPageUrl());
        //    String body = driver.findElement(By.tagName("body")).getText();

            if (parseBody(newPage, tryGetElements(ARTICLE_TEXT, entity.getArticleTextPath()))
             && (parseHeader(newPage, tryGetElement(ARTICLE_NAME, entity.getArticleNamePath())) ||
            parseTags(newPage, entity, tryGetElements(TAGS, entity.getTagsPath())) ||
            parseSimilarLinks(newPage, entity, tryGetElements(SIMILAR_NEWS, entity.getSimilarNewsPath())) ||
            parseDate(newPage, entity, tryGetElement(DATE, entity.getArticleDatePath()))))

            dbConnection.insert(newPage);



            /*System.out.println("Parsed entity " + entity.getEntityUrl() + " in thread #" +
                    Thread.currentThread().getName());
*/


  //          System.out.println("Entity " + entity.getEntityUrl() + " inserted to db in thread #" +
    //                Thread.currentThread().getName());
        }

    }
/*
    public static String getUrlStd(String url) {

        Pattern urlPattern = Pattern.compile(Utils.urlRegex);
        Matcher urlMatcher = urlPattern.matcher(url);
        if (urlMatcher.find()) {
            return urlMatcher.group(2);
        } else return "";
    }
*/
    public String checkWords(String date, String datePattern) throws ParseException {

        String hour, minute, day, month, year;
        LocalDateTime now = LocalDateTime.now();
        date = date.toLowerCase().replaceAll(",", " ").replaceAll(" +", " ").replaceAll("-", " ").trim();
        Calendar calendar = Calendar.getInstance(); // this would default to now

        if ((date.contains("cегодня"))|(date.contains("сегодня"))){
            year = String.valueOf(now.getYear());
            day = String.valueOf(String.format("%02d", now.getDayOfMonth()));
            month = String.valueOf(String.format("%02d", now.getMonthValue()));
            String a = day + "." + month + "." + year;
            date = date.replaceAll("сегодня", a).replaceAll("сегодня", a).replaceAll("[)(]","");
        }

        if (date.toLowerCase().contains("вчера")) {
            //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            calendar.add(Calendar.DATE, -1);
            //System.out.println("date = [" + date + "], datePattern = [" + datePattern + "]");
            year = String.valueOf(now.getYear());
            day = String.valueOf(String.format("%02d", calendar.getTime().getDate()));
            month = String.valueOf(String.format("%02d", calendar.getTime().getMonth()+1));
            String a = day + "." + month + "." + year;
            date = date.replaceAll("вчера", a);
        }

        Locale rusLocale = new Locale.Builder().setLanguage("ru").build();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, rusLocale);

            Date dateFinal = dateFormat.parse(date);

            if (datePattern.contains("yyyy")){
                year = String.valueOf(dateFinal.getYear()+1900);
            }
            else {
                year = String.valueOf(now.getYear());
            }

            day = String.valueOf(String.format("%02d", dateFinal.getDate()));
            month = String.valueOf(String.format("%02d", dateFinal.getMonth() + 1));
            hour = String.valueOf(String.format("%02d", dateFinal.getHours()));
            if( Integer.parseInt(hour) < 0)
                hour = String.valueOf((24 + Integer.parseInt(hour)));
            minute = String.valueOf(String.format("%02d", dateFinal.getMinutes()));
            date = day + "." + month + "." + year + " " + hour + ":" + minute;

        } catch (ParseException e) {
        }
        return date;
    }

    private void dateProc(String date, int monthNumber) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        String result = month + "." + year;
    }

    private String dateRegexpMatch(String date, String regexp) {

            List<String> allMatches = new ArrayList<String>();
            Matcher m = Pattern.compile(regexp).matcher(date);
            while (m.find()) {
                allMatches.add(m.group());
            }

            return allMatches.size() > 0 ? allMatches.get(0) : date;
        }
 /*
    private String articleTextProcessing(String text, WebPage page) {

        String entityUrl = page.getEntityUrl();

        String eraseRegex = "<[a-zA-Z\\/][^>]*>|&nbsp;|\\n";
        String hrefRegex = "(<a.+?\\s*href\\s*=)\\s*[\"\\']?([^\"\\'\\s>]+)[\"\\']?";

        Pattern hrefPattern = Pattern.compile(hrefRegex);
        Matcher hrefMatcher = hrefPattern.matcher(text);

        ArrayList<String> linksFromText = new ArrayList<String>();

        while (hrefMatcher.find()) {
            String foundHref = hrefMatcher.group(2);
            if (!isUrl(foundHref)) {
                foundHref = makeUrl(foundHref, entityUrl);
            } else foundHref = correctUrl(foundHref, entityUrl);
            linksFromText.add(foundHref);
        }

       // if (linksFromText.size() > 0) {
       //     transmitLinksToCrawler(linksFromText, page);
       // }


        //TODO regExp add for &#x97
        String resultText = text.replaceAll(eraseRegex, " ").replaceAll("(?s)<!--.*?-->", " ").replaceAll("&#x200b", " ").replaceAll("&#x97", " ").replaceAll("&nbsp;", " ");
        return resultText;

    }

    private boolean isUrl(String checkedString) {

        Pattern urlPattern = Pattern.compile(urlRegex);
        Matcher urlMatcher = urlPattern.matcher(checkedString);
        if (urlMatcher.find()) {
            return true;
        } else return false;
    }

    private String makeUrl(String interLink, String entityUrl) {

        Pattern urlPattern = Pattern.compile(urlRegex);
        Matcher urlMatcher = urlPattern.matcher(entityUrl);
        String targetUrl = entityUrl;
        if (urlMatcher.find()) {
            targetUrl = urlMatcher.group(2);
        }
        return targetUrl.concat(interLink);
    }

    private String correctUrl(String url, String entityUrl) { //Simple correction for fontanka. Not good, I know.
        String correctedUrl = url;
        System.out.println(url);
        String checkRegex = "(https?:\\/\\/)(www.)(\\w*)\\.(\\w*)((\\W\\w*)*)";
        Pattern checkPattern = Pattern.compile(checkRegex);
        Matcher matcher = checkPattern.matcher(url);
        if (matcher.find() && entityUrl == "http://m.fontanka.ru/") {
            correctedUrl = makeUrl(matcher.group(5), entityUrl);
        }

        return correctedUrl;
    }
  */
    /*private void transmitLinksToCrawler(ArrayList<String> links, WebPage page) {
        entity.transmitToCrawler(links);
    }*/
}
