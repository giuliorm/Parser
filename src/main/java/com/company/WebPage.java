package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by gp on 28.10.15.
 */
public class WebPage implements Runnable {

    private String entityUrl;
    private String pageUrl;
    private String articleNamePath;
    private String articleDatePath;
    private String articleTextPath;

    public WebPage(String entityUrl, String pageUrl, String articleTextPath, String articleDatePath, String articleNamePath) {
        this.entityUrl = entityUrl;
        this.pageUrl = pageUrl;
        this.articleDatePath = articleDatePath;
        this.articleNamePath = articleNamePath;
        this.articleTextPath = articleTextPath;
    }

    public void run() {
        try {
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void parse() throws Exception {
        DBConnection mySqlConnection = new DBConnection();

        Document htmlPage = Jsoup.connect(pageUrl).get();
        ArrayList<String> pageContents = new ArrayList<String>();

        pageContents.add(0, Long.toString(System.nanoTime()));
        Elements header = htmlPage.select(articleNamePath);
        String articleName = header.get(0).textNodes().toString();
        pageContents.add(1, articleName);
        Elements body = htmlPage.select(articleTextPath);
        String articleText = (body).toString();
        pageContents.add(2, articleText);
        //time on main page
        Elements time = htmlPage.select(articleDatePath);
        String articleDate = time.get(0).textNodes().toString().replace("&nbsp;", " ");
        pageContents.add(3, articleDate);
        pageContents.add(4, pageUrl);
        pageContents.add(5, entityUrl);
        mySqlConnection.putIntoDB(pageContents);

        System.out.println("Total number of news in the table : " + mySqlConnection.showNumOfNewsInDB());
    }

}
