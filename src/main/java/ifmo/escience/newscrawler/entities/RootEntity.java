package ifmo.escience.newscrawler.entities;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Alexander Visheratin
 */
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
                    for(int page = 0; page < 83; page++){
                        String addon = "";
                        addon += "&from_day=" + String.valueOf(newDate.getDayOfMonth());
                        addon += "&from_month=" + String.valueOf(newDate.getMonthValue());
                        addon += "&from_year=" + String.valueOf(newDate.getYear());
                        addon += "&to_day=" + String.valueOf(currentDate.getDayOfMonth());
                        addon += "&to_month=" + String.valueOf(currentDate.getMonthValue());
                        addon += "&to_year=" + String.valueOf(currentDate.getYear());
                        addon += "&p=" + String.valueOf(page);
                        List<String> links = getLinks(addon);
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
