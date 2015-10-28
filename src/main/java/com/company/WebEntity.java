package com.company;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebEntity implements Runnable {
    private String entityName;
    private String entityUrl;
    private String newsListPath;
    private String articleNamePath;
    private String articleDatePath;
    private String articleTextPath;
    private long refreshTimeout;


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

    public long getRefreshTimeout() {
        return refreshTimeout;
    }


    //Single-configuration file
    public static WebEntity getEntityFromConfig(String cfgPath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        WebEntity wEntity = mapper.readValue(new File(cfgPath), WebEntity.class);

        return wEntity;

    }

    //Able to load multi-configuration files.
    public static ArrayList<WebEntity> getEntityListFromConfig(String cfgPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<WebEntity> webEntityList = mapper.readValue(new File(cfgPath),
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, WebEntity.class));

        return webEntityList;
    }

    public String saveToConfig(IEntity entity) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File conf = new File("config/myObj.json");
        mapper.writeValue(conf, entity);

        return conf.getAbsolutePath();
    }

    @Override
    public String toString() {
        return (this.entityName
                + "\n" + this.entityUrl
                + "\n" + this.newsListPath);
    }

    public ArrayList<WebPage> getLinksFromTheMainSite(String MainLink) throws Exception {
        Document doc = Jsoup.connect(entityUrl).get();
        Elements blockTitle = doc.select(newsListPath);
        Elements OnlyLinks = blockTitle.select("a[href]");
        ArrayList<WebPage> arrayOfWebPages = new ArrayList<WebPage>();

        String newsUrl = regExp(entityUrl);
        for (int i = 0; i < OnlyLinks.size() - 1; i++) {
            if (((OnlyLinks.get(i)).attr("href").toString()).indexOf("http") == -1) {
                String pageUrl = newsUrl.substring(0, entityUrl.length() - 1)
                        + OnlyLinks.get(i).attr("href").toString();
                WebPage newPage = new WebPage(entityUrl, pageUrl, articleTextPath, articleDatePath, articleNamePath);
                arrayOfWebPages.add(newPage);
            }
        }
        return arrayOfWebPages;
    }


    public void run() {
        try {
            //parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) throws Exception {
        this.run();
    }

    public static String regExp(String entityUrl) throws Exception {
        String LinkAfterRegExp = null;
        Pattern urlPattern = Pattern.compile("^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})\\/");
        Matcher urlMatcher = urlPattern.matcher(entityUrl);
        while (urlMatcher.find()) {
            LinkAfterRegExp = (entityUrl.substring(urlMatcher.start(), urlMatcher.end()) + "");
        }
        return LinkAfterRegExp;
    }
}