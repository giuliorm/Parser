package com.company;

/**
 * Created by gp on 28.10.15.
 */
public class WebPage {

    private WebEntity entity;
    private String entityUrl;
    private String pageUrl;
    private String articleNamePath;
    private String articleDatePath;
    private String articleTextPath;

    public WebEntity getEntity() {
        return entity;
    }

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

    public WebPage(WebEntity entity, String pageUrl) {
        this.entity = entity;
        this.entityUrl = entity.getEntityUrl();
        this.pageUrl = pageUrl;
        this.articleDatePath = entity.getArticleDatePath();
        this.articleNamePath = entity.getArticleNamePath();
        this.articleTextPath = entity.getArticleTextPath();

    }


}
