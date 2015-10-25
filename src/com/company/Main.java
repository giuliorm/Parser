package com.company;

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

