package ifmo.escience.newscrawler;

import ifmo.escience.newscrawler.entities.WebEntity;

import java.util.Date;
import java.util.HashMap;

public class WebPage {
    private Long parseTime;
    private String entityUrl;
    private String pageUrl;
    private String articleName;
    private Date articleDate;
    private String articleText;
    private String tags;
    private String similarNews;



    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setSimilarNews(String sameNews) {
        this.similarNews = sameNews;
    }
    public void setEntityUrl(String entityUrl) {
        this.entityUrl = entityUrl;
    }
    public void setParseTime(Long parseTime) {
        this.parseTime = parseTime;
    }
    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
    public void setArticleDate(Date articleDate) {
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
    public Date getArticleDate() {
        return articleDate;
    }
    public String getArticleText() {
        return articleText;
    }
    public Long getParseTime() {
        return parseTime;
    }
    public String getTags() {
        return tags;
    }
    public String getSimilarNews() {
        return similarNews;
    }

    public WebPage(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        return "";

    }

}
