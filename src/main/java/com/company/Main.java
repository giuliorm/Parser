package com.company;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
    static Log mLog = LogFactory.getLog("MainClassLogger");

    public static void main(String[] args) throws Exception {

        ArrayList<WebEntity> webEntityList = WebEntity.getEntityListFromConfig("src/main/resources/config/multiConfig.json");
        Thread[] threads = new Thread[webEntityList.size()];


   while(true){
        for (int i = 0; i < webEntityList.size(); i++) {
            threads[i] = new Thread(webEntityList.get(i));
            threads[i].start();
            mLog.info("Thread for " + webEntityList.get(i).getEntityName() + " was created");
        }

        Thread.sleep(600000);

        for (int i = 0; i < webEntityList.size(); i++) {
            mLog.info("Thread for " + webEntityList.get(i).getEntityName() + " succesfully ended");
            threads[i].join();

        }

    }
    }
}

