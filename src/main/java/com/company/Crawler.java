package com.company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Easton
 * on 15.11.2015.
 */
public class Crawler {
    static Log mLog = LogFactory.getLog("MainClassLogger");

    public void startDuty() throws IOException, InterruptedException {
        ArrayList<WebEntity> webEntityList = WebEntity.getEntityListFromConfig("src/main/resources/config/multiConfig.json");
        Thread[] threads = new Thread[webEntityList.size()];


        while (true) {
            for (int i = 0; i < webEntityList.size(); i++) {
                threads[i] = new Thread(webEntityList.get(i));
                threads[i].start();
                mLog.info("Thread for " + webEntityList.get(i).getEntityName() + " was created");
            }

            Thread.sleep(600000);

            for (int i = 0; i < webEntityList.size(); i++) {
                threads[i].join();
                mLog.info("Thread for " + webEntityList.get(i).getEntityName() + " succesfully ended");
            }

        }
    }
}
