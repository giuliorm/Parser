package ifmo.escience.newscrawler.entities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class RootEntity extends WebEntity {
    Random random = new Random();
    public RootEntity(WebEntity from){
        this.newsListPath = from.newsListPath;
        this.entityName = from.entityName;
        this.entityUrl = from.entityUrl;
    }
    
    @Override
    public void run(){

        int syncDays = 60;
        StringBuffer addon = new StringBuffer();
        try {
            while(true){
                for(int day = 0; day < syncDays; day++){
                    LocalDateTime currentDate = LocalDateTime.now();
                    LocalDateTime newDate = currentDate.minusDays(1);

//                    System.out.println(t);
                    for(int page = 0; page < 83; page++){
                        int t=5 *(60_000 + random.nextInt(60_000)-30_000);
                        LocalDateTime dateOfParse = LocalDateTime.now();
                        addon.delete(0,addon.length());
                        addon.append(this.entityUrl);
                        addon.append("&from_day=" + String.valueOf(newDate.getDayOfMonth()));
                        addon.append("&from_month=" + String.valueOf(newDate.getMonthValue()));
                        addon.append("&from_year=" + String.valueOf(newDate.getYear()));
                        addon.append("&to_day=" + String.valueOf(currentDate.getDayOfMonth()));
                        addon.append("&to_month=" + String.valueOf(currentDate.getMonthValue()));
                        addon.append("&to_year=" + String.valueOf(currentDate.getYear()));
                        addon.append("&p=" + String.valueOf(page));
                        System.out.println(addon.toString());
                        List<String> links;
                        System.out.println("Start parse");
                        System.out.println(getResponseCode(addon.toString()));
                        if(getResponseCode(addon.toString())==200) {
                            links = getLinks(addon.toString());
                            if (links.isEmpty()) {
                                System.out.println("Не удалось, кулдаун=" + t + "  " + dateOfParse);
                                Thread.sleep(t);
                            } else {
//                                System.out.println("Удалось, " + currentDate1);
                                crawler.addLinks(links);
                                Thread.sleep(t);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error on collecting web pages!", ex);
        }
    }
    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection)url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }
}
