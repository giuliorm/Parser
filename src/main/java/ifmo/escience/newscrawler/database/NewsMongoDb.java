package ifmo.escience.newscrawler.database;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import com.mongodb.client.MongoCollection;
import ifmo.escience.newscrawler.WebPage;
import org.bson.Document;

/**
 * Created by JuriaSan on 16.06.2016.
 */
public class NewsMongoDb extends MongoDbConnection {

    private MongoCollection resources;

    public NewsMongoDb(DbConfig config) {
        super(config);
    }

    @Override
    protected void initCollections() {
        if (config.getCollectionNames().size() > 0) {
            String linksName = config.getCollectionNames().iterator().next();
            resources = db.getCollection(linksName);
        }
    }

    public boolean urlExists(String pageUrl){

        Document doc = new Document();
        doc.append("link", pageUrl);
        return resources.count(doc) > 0;
    }
/*
    public void addMissingLink(String link){

        try{
            BasicDBObject document = (BasicDBObject) resources
                    .find(new BasicDBObject("url", link));

            if(document != null){
                System.out.println(document);
                //?????
                BasicDBObject newDocument =
                        new BasicDBObject().append("$inc",
                                new BasicDBObject().append("count", 1));
                resources.findOneAndUpdate(new BasicDBObject().append("url", link), newDocument);
            }
            else{
                document = new BasicDBObject();
                document.append("url", link);
                document.append("count", 1);
                resources.insertOne(document);
            }
        }
        catch(Exception ex){
            logger.error("Error on adding missing link!", ex);
        }
    }
     */
    private Document documentFromPage(WebPage page) {
        Document doc = new Document();
        doc.append("number",page.getParseTime())
                .append("title", page.getArticleName())
                .append("mainText", page.getArticleText());

        doc.append("date", page.getArticleDate())
                .append("link", page.getPageUrl())
                .append("mainLink",page.getEntityUrl());
        return doc;
    }
    public void insert(WebPage page) {
        try{

            //TODO: how to insert date object?
            resources.insertOne(documentFromPage(page));
        }
        catch(Exception ex){
            logger.error("Error on loading news into database!", ex);
        }
    }

    public void remove(WebPage page) {
        try{

            resources.deleteOne(documentFromPage(page));
        }
        catch(Exception ex){
            logger.error("Error on loading news into database!", ex);
        }

    }
}
