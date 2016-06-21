package ifmo.escience.newscrawler.database;

import ifmo.escience.newscrawler.helpers.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Created by JuriaSan on 15.06.2016.
 */
public class DbConfig{

    private String host;
    private Integer port;
    private String dbName;
    private ArrayList<String> collectionNames;

    static Logger logger =  LogManager.getLogger(NewsMongoDb.class.getName());

    public DbConfig(String host, Integer port, String dbName, String... collectionNames) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.collectionNames = new ArrayList(Arrays.asList(collectionNames));
    }

    public String getHost() {
        return this.host;
    }
    public Integer getPort() {
        return this.port;
    }

    public Collection<String> getCollectionNames() {
        return Collections.unmodifiableCollection(collectionNames);
    }

    public String getDbName() {
        return this.dbName;
    }

    public static DbConfig dbConfigsFromProperties() {

        try{
            String dbHost = ConfigReader.getProperty("dbHost");
            Integer dbPort = Integer.parseInt(ConfigReader.getProperty("dbPort"));
            String dbName = ConfigReader.getProperty("dbName");
            String linksCollection = ConfigReader.getProperty("dbCollection");
            //String dbMissingCollection = ConfigReader.getProperty("dbMissingCollection");
            // mongoClient = new MongoClient(dbHost, dbPort);
            //  db = mongoClient.getDB(dbName);
            //  coll = db.getCollection(dbCollection);
            //  missingCollection = db.getCollection(dbMissingCollection);
            DbConfig config = new DbConfig(dbHost, dbPort, dbName, linksCollection);
            return config;
        }
        catch(IOException ex){
            logger.error("Error on initializing database connection!", ex);
        }
        return null;
    }
}