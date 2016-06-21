package ifmo.escience.newscrawler.helpers;

import ifmo.escience.newscrawler.Crawler;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Alexander Visheratin
 */
public class ConfigReader {

    private  String configFileName;

    public ConfigReader(String configFileName) {
        this.configFileName = configFileName;
    }

    private static Logger logger =  LogManager.getLogger(ConfigReader.class.getName());
    
    public String getProperty(String name) throws IOException {

        FileInputStream inputStream = null;
        String property = null;
        try {

            Properties properties = new Properties();
            inputStream = new FileInputStream(configFileName);

            properties.load(inputStream);
            property = properties.getProperty(name);

        } catch (FileNotFoundException ex) {
            logger.error("Config file not found!", ex);
        } catch (IOException ex) {
            logger.error("Error on reading parameter!", ex);
        }
        finally {
            inputStream.close();
        }
        return property;
    }
}
