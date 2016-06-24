package ifmo.escience.newscrawler.parser;

import ifmo.escience.newscrawler.Utils;
import ifmo.escience.newscrawler.WebPage;
import ifmo.escience.newscrawler.database.NewsMongoDb;
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
import java.util.*;
import java.util.logging.Level;

public class WebPageParser {

    HtmlUnitDriver driver = new HtmlUnitDriver();
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
            //System.out.println("Header is null or empty for entity " + page.getPageUrl());

        page.setArticleName(header.getText());
        return headerString != null && !headerString.isEmpty();

    }

    public boolean parseBody(WebPage page, List<WebElement> bodyElement) {

        if (bodyElement == null)
            return false;

        StringBuilder body = new StringBuilder();
        for (WebElement element : bodyElement) {
            body.append(element.getText());
        }

     //  if (body.length() < 1)
       //     System.out.println("Body is empty for entity " + page.getPageUrl());

        page.setArticleText(body.toString());
        return page.getArticleText() != null && !page.getArticleText().isEmpty();
    }

    public boolean parseSimilarLinks(WebPage page, WebEntity entity, List<WebElement> onlyLinks) {

        List<String> similarLinks = new LinkedList<String>();

        if (!entity.getSimilarNewsPath().isEmpty() && onlyLinks.size() > 0) {

            for (WebElement similarLink : onlyLinks) {
                similarLinks.add(similarLink.getAttribute("href"));
            }
            page.setSimilarNews(similarLinks.toString());
            return true; // TODO: review method because now it is not clear what it does
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
            return true; // TODO: review method because now it is not clear what it does
        }
        return false;
    }

   /* public boolean checkDate(WebEntity entity, Date articleDate) {

        if (articleDate == null)
            return false;
         DateFormat format = new SimpleDateFormat(entity.getDateFormat());
//        String date = dateRegexpMatch(dateString, entity.getRegExpForDate());

            try {
                format.parse(dateString);
            }
            catch(ParseException ex) {


                System.out.println("Date " + dateString + " DOES NOT COMPLY to pattern " + format.toString() + " of entity "
                       + entity != null ? entity.getEntityUrl() : "" +
                       " in thread #" + Thread.currentThread().getName());
                return false;
            }
        System.out.println("Date " + dateString + "  COMPLIES to all patterns in thread #" +
                Thread.currentThread().getName() + " of entity "
                + (entity != null ? entity.getEntityUrl() : ""));
        return true;

    } */

    public boolean parseDate(WebPage page, WebEntity entity, WebElement date) {

        Date articleDate;
        if (date == null || page == null || entity == null)
            return false;

        try {

             System.out.println("Date " + date.getText() + " for url " + page.getPageUrl());

             articleDate = dateFromString(date.getText(), entity.getDateFormat());
           /*  articleDate = checkWords(dateRegexpMatch(date.getText().replaceAll("\n", " "),
                             entity.getRegExpForDate()),
                     entity.getDateFormat()); */
        }
        catch(ParseException pe) {
            logger.error("Parse exception at " + pe.getMessage());
            return false;
        }
        //System.out.println(articleDate + " " + newPage.getPageUrl() + "\n" +Header + "\n" + Body
        //       + "\n" + Tags + similarLinks );
       /* if (!checkDate(entity, articleDate))
        {
            System.out.println("Date " + articleDate + " is NOT COMPLY to suitable format");
            return false;
        }
*/
        page.setArticleDate(articleDate);

        return articleDate != null;
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

           // if (newPage.getPageUrl() == null || newPage.getPageUrl().isEmpty()) {
          //      System.out.println("ACHTUNG! ACHTUNG!");
          //  }

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


/*
    private Date tryGetDate(String date, String format, Locale locale) {

        DateFormat dateFormat = new SimpleDateFormat(format, locale);
        try  {
            return dateFormat.parse(date);
        }
        catch (ParseException e) {

        }
        catch (Exception e) {

        }
        return null;
    }

    private String allMatches(Matcher matcher) {
        List<String> allMatches = new ArrayList<String>();
        while (matcher.find()) {
            allMatches.add(matcher.group());
        }

        return allMatches.size() > 0 ? allMatches.get(0) : null;
    } */

/*
    public Date handleYesterdayWord(String date, String format) {
        LocalDateTime now = LocalDateTime.now();
        Pattern todayPattern = Pattern.compile("(вчера)");
        date = preHandleDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        String year = String.valueOf(now.getYear());
        String day =  String.valueOf(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
        String month = String.valueOf(String.format("%02d", calendar.get(Calendar.MONTH) + 1));
        String dateString = day + "." + month + "." + year;

        Matcher patternMatcher = todayPattern.matcher(date);
        String newDate = patternMatcher.replaceFirst(dateString);

        return tryGetDate(newDate, format, LocaleHolder.getRusLocale());
    }

    public Date handleTodayWord(String date, String format) {
        LocalDateTime now = LocalDateTime.now();
        Pattern todayPattern = Pattern.compile("(сегодня)");

        date = preHandleDate(date);
        String year = String.valueOf(now.getYear());
        String day = String.valueOf(String.format("%02d", now.getDayOfMonth()));
        String month = String.valueOf(String.format("%02d", now.getMonthValue()));
        String dateString = day + "." + month + "." + year;

        Matcher patternMatcher = todayPattern.matcher(date);
        String newDate = patternMatcher.replaceFirst(dateString);

        return tryGetDate(newDate, format, LocaleHolder.getRusLocale());
    } */

    public Date dateFromString(String date, String datePattern) throws ParseException {

        if (date == null || datePattern == null)
            return null;

        //String dateHandled = preHandleDate(date);//.replaceAll(",", " ").replaceAll(" +", " ").replaceAll("-", " ").trim();
        DateFormat format = new SimpleDateFormat(datePattern);
     //   date = preHandleDate(date);
        Date dateFinal = null;
        DateHandler todayHandler = new DateTodayHandler(date, format);
        DateHandler yesterdayHandler = new DateYesterdayHandler(date, format);
        DateHandler dateFromFormat = new DateFromFormatHandler(date, format, false);
        DateHandler endDateTimeFormat = new DateFromFormatHandler(date, format, true);

        DateHandler iterator = dateFromFormat;

        dateFromFormat.setNext(todayHandler);
        todayHandler.setNext(yesterdayHandler);
        yesterdayHandler.setNext(endDateTimeFormat);

        while(iterator.hasNext()) {
            if (dateFinal == null) {
                dateFinal = iterator.handle();
            }
            else break;
            iterator = iterator.getNext();
        }
     /*   Date dateFinal = handleTodayWord(date, datePattern);
        if (dateFinal == null)
            dateFinal = handleYesterdayWord(date, datePattern);

        if (dateFinal == null)
            dateFinal = tryGetDate(date, datePattern, LocaleHolder.getRusLocale());
*/
        return dateFinal;
    }


    /*private void dateProc(String date, int monthNumber) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        String result = month + "." + year;
    } */
/*
    public String dateRegexpMatch(String date, String regexp) {

            if (date == null || regexp == null )
                return  null;

            if(regexp.isEmpty())
                return date;

            List<String> allMatches = new ArrayList<String>();
            Matcher m = Pattern.compile(regexp).matcher(date);
            while (m.find()) {
                allMatches.add(m.group());
            }

            return allMatches.size() > 0 ? allMatches.get(0) : date;
        } */
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
