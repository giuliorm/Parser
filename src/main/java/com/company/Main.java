package com.company;

import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.logging.Level;

public class Main {
    static {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        //TODO: Write TESTs for every SOURCE.


        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);

    }

    private static Logger logger = LogManager.getLogger(Main.class.getName());
    public static void main(String[] args) throws Exception {

        DBConnection.getDbConfigs();
        Crawler crawler = new Crawler();
        logger.trace("Crawler working");
        crawler.startDuty();
    }
}

