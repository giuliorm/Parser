package ifmo.escience.newscrawler;
import ifmo.escience.newscrawler.database.NewsMongoDb;
import ifmo.escience.newscrawler.entities.RootEntity;
import ifmo.escience.newscrawler.entities.WebEntity;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;

public class Crawler {
    static Logger logger =  LogManager.getLogger(Crawler.class.getName());
   // private Map<String, WebEntity> webEntities;

    public static ArrayList<WebEntity> getEntitiesList(String cfgPath) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<WebEntity> webEntityList = mapper.readValue(new File(cfgPath),
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, WebEntity.class));
            return webEntityList;
        }
        catch(IOException ex){
            logger.error("Error on getting links from config!", ex);
            return null;
        }
    }

    public void start(NewsMongoDb connection) throws IOException, InterruptedException {

        ArrayList<RootEntity> rootEntities = new ArrayList<RootEntity>();
        ArrayList<WebEntity> rootEntitiesBase = getEntitiesList("config.json");

        //WebPageParser parser = ;
        HashMap<String, WebEntity> webEntities = fillMap(getEntitiesList("multiConfig.json"));

        for(WebEntity rootBase : rootEntitiesBase){
            RootEntity entity = new RootEntity(rootBase, connection, webEntities);
            rootEntities.add(entity);
        }

        if(webEntities == null){
            logger.error("List of entities in empty!");
            return;
        }

        ExecutorService executor = Executors.newCachedThreadPool();
       //         threadPoolCount(webEntities.size() + rootEntities.size()));

       // for (int i = 0; i < webEntities.size(); i++) {
       //     WebEntity entity = webEntities.get(i);
            //new Thread(entity).start();
        //    executor.execute(entity);
          //  logger.info("Thread for " + entity.getEntityName() + " was created");
      //  }

        logger.info("Starting root entities...");
        for (int i = 0; i < rootEntities.size(); i++) {
            //rootEntities.get(i).setCrawler(this)
           executor.execute(rootEntities.get(i));
           // executor.execute(rootEntities.get(i));
           // new Thread(entity).start();

         //   logger.info("Thread for " + rootEntities.get(i).getEntityName() + " was created");
        }
        executor.shutdown();
        while(!executor.isTerminated()) {

        }

   //     executor.shutdown();
     //   while (!executor.isTerminated()) {
            ;
       // }
    }

    private HashMap<String, WebEntity> fillMap(ArrayList<WebEntity> webEntityList) {
        HashMap<String, WebEntity> webEntities = new HashMap<String, WebEntity>();
        for (int i = 0; i < webEntityList.size(); i++) {
            String entityUrl = webEntityList.get(i).getEntityUrl();
            entityUrl = Utils.getUrlStd(entityUrl);
            //webEntityList.get(i).setCrawler(this);
            webEntities.put(entityUrl, webEntityList.get(i));
        }
        return webEntities;
    }




 /*
    private boolean routeLink(String link) {
        String linkUrl = getUrlStd(link);
        if (webEntities.containsKey(linkUrl)) {
            WebEntity entityForLink = webEntities.get(linkUrl);
            entityForLink.transmitToParser(link);
            return true;
        }
        else{
            dbConnection.addMissingLink(linkUrl);
            return false;
        }
    } */

 /*
    public void addLinks(List<String> links) {
        float goodLinks = 0;
        for (int i = 0; i < links.size(); i++) {
            boolean res = routeLink(links.get(i));
            if(res)
                goodLinks++;
        }
        if (!links.isEmpty())
            System.out.println(goodLinks/links.size()*100);

    } */

}
