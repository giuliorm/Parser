package ifmo.escience.newscrawler.entities;

import ifmo.escience.newscrawler.WebPageParser;
import ifmo.escience.newscrawler.database.NewsMongoDb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




@JsonIgnoreProperties(ignoreUnknown = true)
public class WebEntity implements Runnable {

    static {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
    }

    protected static Logger logger = LogManager.getLogger(WebEntity.class.getName());
    protected String entityName, 
            entityUrl, 
            newsListPath, 
            articleNamePath, 
            articleDatePath, 
            articleTextPath, 
            tags = "", 
            similarNews = "",
            regExpForDate, 
            dateFormat;

    //protected long refreshTimeout;
   // protected Crawler crawler;
    NewsMongoDb connection;


    public WebEntity() {

    }

    public WebEntity(WebEntity from, String url, NewsMongoDb connection) {
        this.entityUrl = url;
        this.connection = connection;

        if (from != null) {
            this.entityName = from.getEntityName();
            this.newsListPath = from.getNewsListPath();
            this.articleNamePath = from.getArticleNamePath();
            this.articleDatePath = from.getArticleDatePath();
            this.articleTextPath = from.getArticleTextPath();
            this.tags = from.getTagsPath();
            this.similarNews = from.getSimilarNewsPath();
            this.regExpForDate = from.getRegExpForDate();
            this.dateFormat = from.getDateFormat();
        }
    }

   // public void setCrawler(Crawler crawler){
   //     this.crawler = crawler;
   // }
    
   /* public void transmitToParser(String link){
        parser.addPage(link);
    }
    
    public void transmitToCrawler(List<String> links){
        crawler.addLinks(links);
    } */

    public NewsMongoDb getConnection() {
        return  this.connection;
    }



    @Override
    public String toString() {
        return (this.entityName
                + "\n" + this.entityUrl
                + "\n" + this.newsListPath);
    }

    protected List<String> getLinks(String targetUrl) throws Exception {

        List<String> arrayOfWebPages = new ArrayList<>();
        HtmlUnitDriver driver = new HtmlUnitDriver();
        logger.trace("Loading links from: " + targetUrl);

        driver.get(targetUrl);

        if(newsListPath!=""){

            List<WebElement> links = driver.findElements(By.xpath(newsListPath));

            for (WebElement link : links) {
                String href = link.getAttribute("href");
                arrayOfWebPages.add(href);
            }
        }

        return arrayOfWebPages;
    }


    @Override
    public void run() {

        System.out.println("Web entity " + getEntityUrl() + "  thread #" + Thread.currentThread().getName() + " is started");

        try {
            new WebPageParser().parsePage(this, connection);
        }
        catch(ParseException e) {
            System.out.println(e.getMessage());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
       /* if (parser == null || this.links == null || this.links.size() < 1)
            return;

        try {
            //while(true){
            parser.parse(this);
            // Thread.sleep(refreshTimeout); //what for?
            // }
        } catch (Exception e) {
            logger.error("Error on collecting web pages!", e);
        }
        */
        System.out.println("Web entity " + getEntityUrl() + " thread #" + Thread.currentThread().getName() + " is exiting");
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

    public String getTagsPath() {
        return tags;
    }

    public String getSimilarNewsPath() {
        return similarNews;
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
