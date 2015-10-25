package com.company;

import java.util.ArrayList;

public class Main {


    //http://www.gazeta.spb.ru/allnews/
    //http://www.fontanka.ru/
/*
    private String entityName = "site";
    public String entityUrl = "http://m.fontanka.ru/";
    private String newsListPath = "li.article.switcher-all-news";
    private String articleNamePath = "h2.itemTitle";
    private String articleDatePath = "span.itemDateCreated";
    private String articleTextPath = "div.itemFullText";
    */


    public static void main(String[] args) throws Exception {
       /* WebEntity[] we = new WebEntity[2];
        we[0] = new WebEntity("site", "http://www.gazeta.spb.ru/allnews/", "div.materials.nonLine",
                "h1", "i", "div#ntext", 600000);
        we[1] = new WebEntity("site", "http://m.fontanka.ru/", "li.article.switcher-all-news", "h2.itemTitle",
                "span.itemDateCreated", "div.itemFullText", 600000);

        Thread[] threads = new Thread[2];
        for (int i = 0; i < we.length; i++) {
            threads[i] = new Thread(we[i]);
            threads[i].start();
        }

        for (int i=0; i< we.length; i++)
        {
            threads[i].join();
        }

*/
        //   ArrayList<WebEntity> we = new ArrayList<WebEntity>();

        // we.getEntityListFromConfig("config/multiConfig.json");
        WebEntity we = new WebEntity();
        //for (WebEntity entityInstance : we.getEntityListFromConfig("src/main/resources/config/multiConfig.json")) {
        //};
        ArrayList<WebEntity> webEntityList = we.getEntityListFromConfig("src/main/resources/config/multiConfig.json");

        Thread[] threads = new Thread[2];
        for (int i = 0; i < webEntityList.size(); i++) {
            threads[i] = new Thread(webEntityList.get(i));
            threads[i].start();
        }

        for (int i = 0; i < webEntityList.size(); i++) {
            threads[i].join();
        }
        //we = we.getEntityFromConfig("config/config.json");
        //System.out.println(we.toString());
        //we.parse();

    }


}

