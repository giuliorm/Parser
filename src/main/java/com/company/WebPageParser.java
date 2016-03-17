package com.company;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebPageParser {
    WebDriver driver = new HtmlUnitDriver();

    private static Logger WPPlogger = LogManager.getLogger(WebPageParser.class.getName());

    DBConnection dbConnection = new DBConnection();

    private ArrayList<WebPage> arrayOfWebPage;
    static {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
    }

    public WebPageParser(ArrayList<WebPage> arrayOfWebPage) {
        this.arrayOfWebPage = arrayOfWebPage;
    }

    public void addPage(WebPage page) {
        arrayOfWebPage.add(page);
    }

    public void run() {
        try {
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse() {
        while (arrayOfWebPage.size() != 0) {
            try {
                parsePage(arrayOfWebPage.get(0));
            } catch (Exception e) {
                e.printStackTrace();
                WPPlogger.error("Web page is not starting " + arrayOfWebPage.get(0).getPageUrl());
                System.out.println("Web page is not starting " + arrayOfWebPage.get(0).getPageUrl());
            }
            arrayOfWebPage.remove(0);
        }
    }

    private void parsePage(WebPage webPage) throws Exception {
        if (!dbConnection.IsExist(webPage.getPageUrl())) {
            webPage.setParseTime(System.nanoTime());

            driver.get(webPage.getPageUrl());
            String Header = driver.findElement(By.xpath(webPage.getArticleName())).getText();
            List<WebElement> BodyElement = driver.findElements(By.xpath(webPage.getArticleText()));
            String Body = "";
            for (WebElement element : BodyElement) {
                Body += element.getText();
            }

            String Tags = null;
            if (!webPage.getTags().isEmpty()) {
                List<WebElement> TagsElement = driver.findElements(By.xpath(webPage.getTags()));
                List<String> tagsList = new LinkedList<String>();
                for (WebElement element : TagsElement) {
                    tagsList.add(element.getText());
                }
                Tags = tagsList.toString();
            }

            List<String> links = new LinkedList<String>();
            if (!webPage.getSameNews().isEmpty()) {
                List<WebElement> OnlyLinks = driver.findElements(By.xpath(webPage.getSameNews()));
                for (WebElement link : OnlyLinks) {
                    links.add(link.getAttribute("href"));
                }
            }

            String date = driver.findElement(By.xpath(webPage.getArticleDate())).getText();
            String articleDate = doRegExp(webPage, checkWords(date, webPage.getDateFormat()));
            webPage.setArticleName(Header);
            webPage.setArticleText(Body);
            webPage.setArticleDate(articleDate);
            webPage.setSameNews(links.toString());
            webPage.setTags(Tags);

            System.out.println(webPage.getPageUrl());
            System.out.println(webPage.getArticleName());
            System.out.println(webPage.getArticleDate());
            System.out.println(webPage.getArticleText());
            System.out.println(webPage.getTags());
            System.out.println(webPage.getSameNews());


            dbConnection.putIntoDB(webPage);
        }
    }
    private String checkWords(String date, String datePattern) throws ParseException {
        String hour, minute, day, month, year;
        LocalDateTime now = LocalDateTime.now();
        date = date.toLowerCase().replaceAll(",", " ").replaceAll(" +", " ");


        if (date.contains("сегодня")) {
            year = String.valueOf(now.getYear());
            day = String.valueOf(String.format("%02d", now.getDayOfMonth()));
            month = String.valueOf(String.format("%02d", now.getMonthValue()));
            String a = day + "." + month + "." + year;
            date = date.replaceAll("сегодня", a);
        }

        if (date.contains("вчера")) {
            year = String.valueOf(now.getYear());
            day = String.valueOf(String.format("%02d", now.getDayOfMonth()));
            month = String.valueOf(String.format("%02d", now.getMonthValue()));
            String a = day + "." + month + "." + year;
            date = date.replaceAll("вчера", a);
        }

        Locale rusLocale = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern, rusLocale);
            dateFormat.setTimeZone(TimeZone.getTimeZone("МСК"));
            Date dateFinal = dateFormat.parse(date);

            if (datePattern.contains("yyyy")){
                year = String.valueOf(dateFinal.getYear()+1900);
                //System.out.println(dateFinal);
            }
            else {
                year = String.valueOf(now.getYear());
            }

            day = String.valueOf(String.format("%02d", dateFinal.getDate()));
            month = String.valueOf(String.format("%02d", dateFinal.getMonth() + 1));
            hour = String.valueOf(String.format("%02d", dateFinal.getHours()-3));
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

    private String doRegExp(WebPage webPage, String date) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(webPage.getRegExpForDate()).matcher(date);
        while (m.find()) {
            allMatches.add(m.group());
        }

        return allMatches.size()>0? allMatches.get(0) : date;
    }


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

        if (linksFromText.size() > 0) {
            transmitLinksToCrawler(linksFromText, page);
        }

        //TODO regExp add for &#x97
        String resultText = text.replaceAll(eraseRegex, "").replaceAll("(?s)<!--.*?-->", "").replaceAll("&#x200b", " ").replaceAll("&#x97", "").replaceAll("&nbsp;", " ");
        return resultText;

    }

    private boolean isUrl(String checkedString) {
        String urlRegex = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?" +
                "[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)" +
                "((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[.\\!\\/\\\\w]*))?)";
        Pattern urlPattern = Pattern.compile(urlRegex);
        Matcher urlMatcher = urlPattern.matcher(checkedString);
        if (urlMatcher.find()) {
            return true;
        } else return false;
    }

    private String makeUrl(String interLink, String entityUrl) {
        String urlRegex = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?" +
                "[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)" +
                "((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[.\\!\\/\\\\w]*))?)";
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

    private void transmitLinksToCrawler(ArrayList<String> links, WebPage page) {
        page.getEntity().transmitToCrawler(links);
    }
}
