package ifmo.escience.newscrawler.entities;

import java.time.LocalDateTime;
import java.util.List;

public class RootEntity extends WebEntity {
    
    public RootEntity(WebEntity from){
        this.newsListPath = from.newsListPath;
        this.entityName = from.entityName;
        this.entityUrl = from.entityUrl;
    }
    
    @Override
    public void run(){
        int syncDays = 60;
        try {
            while(true){
                LocalDateTime currentDate = LocalDateTime.now();
                for(int day = 0; day < syncDays; day++){
                    LocalDateTime newDate = currentDate.minusDays(1);
                    StringBuffer addon = new StringBuffer();
                    for(int page = 0; page < 83; page++){
                        Thread.sleep(1000);
                        addon.append(this.entityUrl);
                        addon.append("&from_day=" + String.valueOf(newDate.getDayOfMonth()));
                        addon.append("&from_month=" + String.valueOf(newDate.getMonthValue()));
                        addon.append("&from_year=" + String.valueOf(newDate.getYear()));
                        addon.append("&to_day=" + String.valueOf(currentDate.getDayOfMonth()));
                        addon.append("&to_month=" + String.valueOf(currentDate.getMonthValue()));
                        addon.append("&to_year=" + String.valueOf(currentDate.getYear()));
                        addon.append("&p=" + String.valueOf(page));
                        System.out.println(addon.toString());
                        List<String> links = getLinks(addon.toString());
                        crawler.addLinks(links);
                        Thread.sleep(10000);
                    }
                    currentDate = newDate;
                }
            }
        } catch (Exception ex) {
            logger.error("Error on collecting web pages!", ex);
        }
    }
}
