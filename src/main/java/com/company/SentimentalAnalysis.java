package com.company;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Easton
 * on 06.12.2015.
 */
public class SentimentalAnalysis {

    private static final String url = "http://gateway-a.watsonplatform.net/calls/text/TextGetTextSentiment";
    private static final String apiKey = "7bb5d7dbf759e3d7a9ee7bf9fca18e3c240c2d55";

    private int amountOfPositiveNews = 0;
    private int amountOfNegativeNews = 0;
    private int amountOfProcessedNews = 0;

    //alchemyapi.com
    public void checkViaAlchemy(String text) {
        String requestString = url + "?apikey=" + apiKey + "&text=" + text + "&outputMode=json";
        try {
            Document response = Jsoup.connect(requestString).ignoreContentType(true).get();
            String eraseRegex = "<[a-zA-Z\\/][^>]*>";
            String result = response.toString();
            result = result.replaceAll(eraseRegex, "");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(result);
            String score = root.path("docSentiment").path("score").toString();
            String type = root.path("docSentiment").path("type").toString();
            if (type.contains("negative")) {
                amountOfNegativeNews++;
            } else amountOfPositiveNews++;
            amountOfProcessedNews++;
            System.out.println(result + score + " ololololo  " + type + amountOfPositiveNews + amountOfNegativeNews + amountOfProcessedNews);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SentimentalAnalysis sa = new SentimentalAnalysis();
        sa.checkViaAlchemy("Лекция будет доступна только 24 часа, а потом автоматически пропадет, так что не откладывайте просмотр надолго. А подписавшись на мой профиль, вы получите уведомление о новых трансляциях, чтобы задать ваши вопросы в лайв-режиме.");
    }
}
