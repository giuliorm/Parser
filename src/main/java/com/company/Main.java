package com.company;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws Exception {

        ArrayList<WebEntity> webEntityList = WebEntity.getEntityListFromConfig("src/main/resources/config/multiConfig.json");

        Thread[] threads = new Thread[webEntityList.size()];
        for (int i = 0; i < webEntityList.size(); i++) {
            threads[i] = new Thread(webEntityList.get(i));
            threads[i].start();
        }

        for (int i = 0; i < webEntityList.size(); i++) {
            threads[i].join();
        }
    }


}

