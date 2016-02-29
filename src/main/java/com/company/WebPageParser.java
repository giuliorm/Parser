package com.company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vyakovlev on 11/7/2015.
 */
public class WebPageParser {
    static Log LogWebPage = LogFactory.getLog("MainClassLogger");

    private ArrayList<WebPage> arrayOfWebPage;

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
                LogWebPage.info("Web page is not starting " + arrayOfWebPage.get(0).getPageUrl());
            }
            /*ArrayList<WebPage> pageList = takeWebPagesFromEntity(arrayOfWebPage.get(0));
            if (pageList.size() > 0) {
                arrayOfWebPage.addAll(pageList);
            }*/
            arrayOfWebPage.remove(0);
        }
    }

    private void parsePage(WebPage webPage) throws Exception {
        DBConnection mySqlConnection = new DBConnection();

        Document htmlPage = Jsoup.connect(webPage.getPageUrl()).get();
        ArrayList<String> pageContents = new ArrayList<String>();

        pageContents.add(0, Long.toString(System.nanoTime()));
        Elements header = htmlPage.select(webPage.getArticleNamePath());
        String articleName = header.get(0).textNodes().toString();
        pageContents.add(1, articleName);
        Elements body = htmlPage.select(webPage.getArticleTextPath());
        String articleText = (body).toString();
        //added text preprocessing. may be a bottleneck.
        pageContents.add(2, articleTextProcessing(articleText, webPage));
        //time on main page
        Elements time = htmlPage.select(webPage.getArticleDatePath());
        String articleDate = time.get(0).textNodes().toString().replace("&nbsp;", " ");
        pageContents.add(3, articleDate);
        pageContents.add(4, webPage.getPageUrl());
        pageContents.add(5, webPage.getEntityUrl());
        mySqlConnection.putIntoDB(pageContents);

     /*   String sentimentValue = SentimentalAnalysis.checkViaAlchemy(pageContents.get(2));
        mySqlConnection.putSentimentToDB(sentimentValue,
                pageContents.get(0),
     *//*           pageContents.get(3));*/
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

        String resultText = text.replaceAll(eraseRegex, "");

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
