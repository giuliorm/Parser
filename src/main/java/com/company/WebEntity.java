package com.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class WebEntity implements Runnable {

    private static Logger WElogger = LogManager.getLogger(WebPageParser.class.getName());
    private String entityName, entityUrl, newsListPath, articleNamePath, articleDatePath, articleTextPath, tags, sameNews, regExpForDate, dateFormat;
    private long refreshTimeout;

    private Crawler crawlerForCurrentEntity;
    private WebPageParser parserForCurrentEntity;
    static {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
    }

    public static ArrayList<WebEntity> getEntityListFromConfig(String cfgPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<WebEntity> webEntityList = mapper.readValue(new File(cfgPath),
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, WebEntity.class));

        return webEntityList;
    }

    @Override
    public String toString() {
        return (this.entityName
                + "\n" + this.entityUrl
                + "\n" + this.newsListPath);
    }

    private ArrayList<WebPage> getLinksFromTheMainSite(String MainLink) throws Exception {
        ArrayList<WebPage> arrayOfWebPages = new ArrayList<WebPage>();

        WebDriver driver = new HtmlUnitDriver();

        driver.get(entityUrl);

        List<WebElement> OnlyLinks = driver.findElements(By.xpath(newsListPath));

        for (WebElement link : OnlyLinks) {
            WebPage newPage = new WebPage(this, link.getAttribute("href"));
            arrayOfWebPages.add(newPage);
        }

        for (WebPage wp : arrayOfWebPages) {
            wp.setTags(tags);
            wp.setSameNews(sameNews);
            wp.setRegExpForDate(regExpForDate);
            wp.setDateFormat(dateFormat);
        }

        WElogger.info("");
        return arrayOfWebPages;
    }

    public void run() {
        WebPageParser pageParser = null;
        try {
            pageParser = new WebPageParser(getLinksFromTheMainSite(entityUrl));
            parserForCurrentEntity = pageParser;
            pageParser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) throws Exception {
        this.run();
    }

    public void transmitToCrawler(ArrayList<String> links) {
        crawlerForCurrentEntity.addLinks(links);
    }

    public void transmitToParser(String link) {
        WebPage page = new WebPage(this, link);
        parserForCurrentEntity.addPage(page);
    }


    public void setCrawlerForCurrentEntity(Crawler crawlerForCurrentEntity) {
        this.crawlerForCurrentEntity = crawlerForCurrentEntity;
    }

    public String getArticleTextPath() {
        return articleTextPath;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getNewsListPath() {
        return newsListPath;
    }

    public String getArticleNamePath() {
        return articleNamePath;
    }

    public String getArticleDatePath() {
        return articleDatePath;
    }

    public String getTags() {
        return tags;
    }

    public String getSameNews() {
        return sameNews;
    }

    public long getRefreshTimeout() {
        return refreshTimeout;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getRegExpForDate() {
        return regExpForDate;
    }

    public void setRegExpForDate(String regExpForDate) {
        this.regExpForDate = regExpForDate;
    }

}
