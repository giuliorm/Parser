package com.company;

/**
 * Created by gp on 28.10.15.
 */
public class WebPage {

    private WebEntity entity;
    private Long parseTime;
    private String entityUrl;
    private String pageUrl;
    private String articleName;
    private String articleDate;
    private String articleText;
    private String parserMode;

    public WebEntity getEntity() {
        return entity;
    }


    public Long parseTime() {
        return parseTime;
    }

    public void setEntity(WebEntity entity) {
        this.entity = entity;
    }

    public void setParseTime(Long parseTime) {
        this.parseTime = parseTime;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getArticleName() {
        return articleName;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public String getArticleText() {
        return articleText;
    }

    public String getParserMode() {
        return parserMode;
    }

    public WebPage(WebEntity entity, String pageUrl) {
        this.entity = entity;
        this.entityUrl = entity.getEntityUrl();
        this.pageUrl = pageUrl;
        this.articleDate = entity.getArticleDatePath();
        this.articleName = entity.getArticleNamePath();
        this.articleText = entity.getArticleTextPath();
        this.parserMode = entity.getParserMode();
    }

}
