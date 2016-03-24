package ifmo.escience.newscrawler;


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
                        " Мо�?ква. 8 декабр�?. INTERFAX.RU - Губернатор Сева�?топол�? Сергей Мен�?йло ра�?пор�?дил�?�? �?о вторника, 8 декабр�?, начать по�?тапно во�?�?танавливать прерванную из-за ЧС на Крым�?ком полуо�?трове работу дет�?ких �?адов и выпу�?кать на улицы города ча�?ть троллейбу�?ов, �?ообщила пре�?�?-�?лужба �?ева�?тополь�?кого правитель�?тва. \n" +
                        " По �?о�?то�?нию на вечер 7 декабр�? Сева�?тополю у�?тановлен лимит �?нергопотреблени�? в 165 МВт. Это без учета �?об�?твенной генерации и �?лектро�?нергии, которую дают генераторы большой мощно�?ти. В�?е котельные города работают. \n" +
                        " \"В �?оответ�?твии �? решением губернатора Мен�?йло до утра 8 декабр�? дет�?кие �?ады на территории города будут обе�?печены �?лектро�?нергией. По�?ле мы �?мотрим, что не выходим за имеющий�?�? лимит. Также губернатором дано указание выве�?ти на �?оциально значимые маршруты определенное количе�?тво троллейбу�?ов. График подачи �?лектро�?нергии по районам города пока не мен�?ет�?�?, решение о введении нового графика будет прин�?то по�?ле обе�?печени�? дет�?ких �?адов и начала движени�? троллейбу�?ов, о�?новыва�?�?ь на данных �?нергобалан�?а\", - цитируют�?�? в �?ообщении пре�?�?-�?лужбы �?лова директора департамента город�?кого хоз�?й�?тва Сева�?топол�? Олега Казурина. \n" +
                        " По�?ле запу�?ка объектов �?оциальной �?феры Сева�?топол�? будет ра�?�?мотрен вопро�? подключени�? по по�?то�?нной �?хеме промышленных предпри�?тий города, которые уча�?твуют в выполнении го�?удар�?твенного оборонного заказа. Им необходимо минимум 2,2 МВт. \n" +
                        " Крым�?кий полуо�?тров был полно�?тью обе�?точен в ночь на 22 но�?бр�? в результате подрыва двух опор ЛЭП, идущих из Украины. Сейча�? �?нерго�?и�?тема региона работает в изолированном режиме, дл�? �?кономии примен�?ют�?�? веерные отключени�?. \n" +
                        " Вечером 2 декабр�? полуо�?тров начал получать первую �?лектро�?нергию �? территории Ро�?�?ии. В церемонии опережающего запу�?ка первой цепи подводного �?нергомо�?та через Керчен�?кий пролив прин�?л уча�?тие президент РФ Владимир Путин. \n" +
                        "&outputMode=json ";
        sa.checkViaAlchemy(str);
    }
}
