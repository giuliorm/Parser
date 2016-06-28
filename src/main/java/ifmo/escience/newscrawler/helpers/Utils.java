package ifmo.escience.newscrawler.helpers;

import ifmo.escience.newscrawler.entities.WebEntity;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JuriaSan on 19.06.2016.
 */
public class Utils {

    public  static String urlRegex = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?" +
            "[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)" +
            "((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[.\\!\\/\\\\w]*))?)";


    public static ArrayList<WebEntity> getEntitiesList(String cfgPath) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<WebEntity> webEntityList = mapper.readValue(new File(cfgPath),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, WebEntity.class));
            return webEntityList;
        }
        catch(IOException ex){
            System.out.println("Error loading entitites from file! " + ex.getMessage());
            return null;
        }
    }

    public static String getUrlStd(String url) {

        Pattern urlPattern = Pattern.compile(urlRegex);
        Matcher urlMatcher = urlPattern.matcher(url);
        if (urlMatcher.find()) {
            return urlMatcher.group(2);
        } else return "";
    }

    public static int threadPoolCount(int tasksCount) {

        int minThreads = 10;
        int maxThreads = 50;
        int divisor = 2;

        int threads = tasksCount / divisor;

        if (tasksCount < minThreads)
            threads = tasksCount;

        if (threads > maxThreads)
            threads = maxThreads;

        return  threads;
    }
}
