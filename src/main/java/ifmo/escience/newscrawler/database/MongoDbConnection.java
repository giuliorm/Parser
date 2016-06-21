package ifmo.escience.newscrawler.database;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ifmo.escience.newscrawler.WebPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.util.HashMap;


public class MongoDbConnection {

    protected MongoClient client;
    protected MongoDatabase db;
    protected DbConfig config;

    protected static Logger logger =  LogManager.getLogger(MongoDbConnection.class.getName());

    public MongoDbConnection(DbConfig config) {
        this.config = config;
    }

    public void connect () throws Exception {
        try {
            client = new MongoClient(config.getHost(), config.getPort());
            db = client.getDatabase(config.getDbName());
        }
        catch(Exception ex) {
            logger.error("Error while connecting to the database !", ex);
        }

        initCollections();
    }

    public void clear() {
        for (String collectionName : config.getCollectionNames()) {
            db.getCollection(collectionName).deleteMany(new Document());
        }
    }

    protected void initCollections () {

    }

    public void close() {
        try {
            client.close();
        }
        catch(Exception e) {
            logger.error("Exception while trying to close the database client: " + e.getMessage() );
        }
    }
}




/*
public class DBConnection {
    static MongoClient mongoClient;
    static DB db;
    static DBCollection coll;
    static DBCollection missingCollection;
   // static Logger logger =  LogManager.getLogger(DBConnection.class.getName());

    public void insert(WebPage page) {
        try{
            BasicDBObject doc = new BasicDBObject();
            doc.append("Number",page.parseTime()).append("Title", page.getArticleName()).append("MainText", page.getArticleText());
            doc.append("Date", page.getArticleDate()).append("Link", page.getPageUrl()).append("MainLink",page.getEntityUrl());
            coll.insert(doc);
        }
        catch(Exception ex){
            logger.error("Error on loading news into database!", ex);
        }
    }

    public boolean urlExists(String pageUrl){
        return coll.find(new BasicDBObject("Link", pageUrl)).count() > 0 ;
    }
    
    public void addMissingLink(String link){
        try{
            BasicDBObject document = (BasicDBObject) missingCollection.findOne(new BasicDBObject("url", link));
        if(document != null){
            System.out.println(document);
            BasicDBObject newDocument =
                    new BasicDBObject().append("$inc",
                            new BasicDBObject().append("count", 1));
            missingCollection.update(new BasicDBObject().append("url", link), newDocument);
        }
        else{
            document = new BasicDBObject();
            document.append("url", link);
            document.append("count", 1);
            missingCollection.insert(document);
        }
        }
        catch(Exception ex){
            logger.error("Error on adding missing link!", ex);
        }
    }

    public static void getDbConfigs() {
        try{
            String dbHost = ConfigReader.getProperty("dbHost");
            Integer dbPort = Integer.parseInt(ConfigReader.getProperty("dbPort"));
            String dbName = ConfigReader.getProperty("dbName");
            String dbCollection = ConfigReader.getProperty("dbCollection");
            String dbMissingCollection = ConfigReader.getProperty("dbMissingCollection");
            mongoClient = new MongoClient(dbHost, dbPort);
            db = mongoClient.getDB(dbName);
            coll = db.getCollection(dbCollection);
            missingCollection = db.getCollection(dbMissingCollection);
        }
        catch(IOException ex){
            logger.error("Error on initializing database connection!", ex);
        }
    }
}
*/