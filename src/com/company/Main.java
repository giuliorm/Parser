package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.ArrayList;


public class Main {


    //http://www.gazeta.spb.ru/allnews/
    //http://www.fontanka.ru/



    public static void main(String[] args) throws Exception {
        WebEntity we = new WebEntity();
        we = we.entityFromCfg("config/config.json");
        //System.out.println(we.toString());
        we.parse();
    }


}

