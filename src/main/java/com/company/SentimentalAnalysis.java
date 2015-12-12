package com.company;


import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;

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
    public static String checkViaAlchemy(String text) {
        String requestString = url + "?apikey=" + apiKey + "&text=" + text + "&outputMode=json";
        try {
            Document response = Jsoup.connect(requestString).ignoreContentType(true).get();
            String eraseRegex = "<[a-zA-Z\\/][^>]*>";
            String result = response.toString();
            result = result.replaceAll(eraseRegex, "");

            System.out.println(result);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(result);
            String score = root.path("docSentiment").path("score").toString();
            score = score.substring(1, score.length() - 1); //Jackson's toString output is a little bit strange
            String type = root.path("docSentiment").path("type").toString();
            type = type.substring(1, type.length() - 1);
            /*if (type.contains("negative")) {
                amountOfNegativeNews++;
            } else amountOfPositiveNews++;
            amountOfProcessedNews++;*/
            System.out.println(result + score + " " + type);
            if (score.length() == 0) {
                return "0";
            } else return score;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println(requestString);
            return "0";
        }
        catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            return "0";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
    }

    private void addSentimentInfoToDB(String score) {


    }

    public static void main(String[] args) {
        SentimentalAnalysis sa = new SentimentalAnalysis();
        String str =
                "http://gateway-a.watsonplatform.net/calls/text/TextGetTextSentiment?apikey=7bb5d7dbf759e3d7a9ee7bf9fca18e3c240c2d55&text= \n" +
                        " Москва. 8 декабря. INTERFAX.RU - Губернатор Севастополя Сергей Меняйло распорядился со вторника, 8 декабря, начать поэтапно восстанавливать прерванную из-за ЧС на Крымском полуострове работу детских садов и выпускать на улицы города часть троллейбусов, сообщила пресс-служба севастопольского правительства. \n" +
                        " По состоянию на вечер 7 декабря Севастополю установлен лимит энергопотребления в 165 МВт. Это без учета собственной генерации и электроэнергии, которую дают генераторы большой мощности. Все котельные города работают. \n" +
                        " \"В соответствии с решением губернатора Меняйло до утра 8 декабря детские сады на территории города будут обеспечены электроэнергией. После мы смотрим, что не выходим за имеющийся лимит. Также губернатором дано указание вывести на социально значимые маршруты определенное количество троллейбусов. График подачи электроэнергии по районам города пока не меняется, решение о введении нового графика будет принято после обеспечения детских садов и начала движения троллейбусов, основываясь на данных энергобаланса\", - цитируются в сообщении пресс-службы слова директора департамента городского хозяйства Севастополя Олега Казурина. \n" +
                        " После запуска объектов социальной сферы Севастополя будет рассмотрен вопрос подключения по постоянной схеме промышленных предприятий города, которые участвуют в выполнении государственного оборонного заказа. Им необходимо минимум 2,2 МВт. \n" +
                        " Крымский полуостров был полностью обесточен в ночь на 22 ноября в результате подрыва двух опор ЛЭП, идущих из Украины. Сейчас энергосистема региона работает в изолированном режиме, для экономии применяются веерные отключения. \n" +
                        " Вечером 2 декабря полуостров начал получать первую электроэнергию с территории России. В церемонии опережающего запуска первой цепи подводного энергомоста через Керченский пролив принял участие президент РФ Владимир Путин. \n" +
                        "&outputMode=json ";
        sa.checkViaAlchemy(str);
    }
}
