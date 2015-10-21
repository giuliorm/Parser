package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.ArrayList;


public class Main {


    //http://www.gazeta.spb.ru/allnews/
    //http://www.fontanka.ru/

    // ArrayList<String> PackOfSettingsGazeta = new ArrayList<String>("http://www.gazeta.spb.ru/allnews/", 9,
    // "div.materials.nonLine", "h1",  "div#ntext", "i" );
    // ArrayList<String> PacjOfSettingsFontan = new ArrayList<String>("http://m.fontanka.ru/", 1,
    // "li.article.switcher-all-news", "h2.itemTitle", "div.itemFullText", "span.itemDateCreated");

    public void main(String[] args) throws Exception {
        WebEntity we = new WebEntity();
        we = we.entityFromCfg("config/config.json");
        //System.out.println(we.toString());
        we.parse();
    }


}

