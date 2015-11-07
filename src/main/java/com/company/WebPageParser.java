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

    public void run() {
        try {
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse() {
        for (int i = 0; i < arrayOfWebPage.size(); i++) {
            try {
                parsePage(arrayOfWebPage.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                LogWebPage.info("Web page is not starting" + arrayOfWebPage.get(i).getPageUrl());
            }
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
        pageContents.add(2, articleText);
        //time on main page
        Elements time = htmlPage.select(webPage.getArticleDatePath());
        String articleDate = time.get(0).textNodes().toString().replace("&nbsp;", " ");
        pageContents.add(3, articleDate);
        pageContents.add(4, webPage.getPageUrl());
        pageContents.add(5, webPage.getEntityUrl());
        mySqlConnection.putIntoDB(pageContents);
    }

    public static String articleTextProcessing(String text) {
        String eraseRegex = "<[a-zA-Z\\/][^>]*>";
        String hrefRegex = "<a.+?\\s*href\\s*=\\s*[\"\\']?([^\"\\'\\s>]+)[\"\\']?";

        Pattern hrefPattern = Pattern.compile(hrefRegex);
        Matcher hrefMatcher = hrefPattern.matcher(text);

        while (hrefMatcher.find()) {
            System.out.println(hrefMatcher.group());
            String foundLink = hrefMatcher.group();
            //foundLink.
        }

        String resultText = text.replaceAll(eraseRegex, "");

        //System.out.println(resultText);

        return resultText;

    }

    public static void main(String[] args) {
        String testString = "<p>\n" +
                "\tВ 2014 году тремя организациями было доставлено на спецплощадки 174 960 автомобилей. Что должно было обойтись автовладельцам в 472 392 000 рублей, а с учетом платы за хранение (в среднем более пяти часов на автомобиль) &ndash; в полмиллиарда с лишним.</p>\n" +
                "<p>\n" +
                "\t<a href=\"http://http://www.fontanka.ru/2015/06/19/133/\">В июне 2015 года были объявлены новые правила эвакуации</a>, казалось бы, ужесточившие требования к ДПС и эвакуаторщикам. По словам представителей уполномоченных организаций, количество доставляемых на спецплощадки авто упало в разы и угрожало разорением. Однако <a href=\"http://www.fontanka.ru/2015/08/19/103/\">промежуточные итоги года поразительны</a>.</p>\n";
        WebPageParser.articleTextProcessing(testString);

    }
}
