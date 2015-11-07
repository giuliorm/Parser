package com.company;

/**
 * Created by gp on 28.10.15.
 */
public class WebPage {
    private String entityUrl;
    private String pageUrl;
    private String articleNamePath;
    private String articleDatePath;
    private String articleTextPath;

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getArticleNamePath() {
        return articleNamePath;
    }

    public String getArticleDatePath() {
        return articleDatePath;
    }

    public String getArticleTextPath() {
        return articleTextPath;
    }

    public WebPage(String entityUrl, String pageUrl, String articleTextPath, String articleDatePath, String articleNamePath) {
        this.entityUrl = entityUrl;
        this.pageUrl = pageUrl;
        this.articleDatePath = articleDatePath;
        this.articleNamePath = articleNamePath;
        this.articleTextPath = articleTextPath;
    }


}
