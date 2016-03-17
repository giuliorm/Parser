package com.company;

import com.mongodb.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DBConnection {
    static ArrayList dbConfig;
    static MongoClient mongoClient;
    static DB db ;
    static DBCollection coll ;


    public void putIntoDB(WebPage page) {
        try{
            System.out.println("Connect to database successfully");
            BasicDBObject doc = new BasicDBObject();
            doc.append("Number",page.parseTime()).append("Title", page.getArticleName()).append("MainText", page.getArticleText());
            doc.append("Date", page.getArticleDate()).append("Link", page.getPageUrl()).append("MainLink",page.getEntityUrl());
            coll.insert(doc);
            System.out.println("Document inserted successfully");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public boolean IsExist(String pageUrl ){
        return (coll.find(new BasicDBObject("Link", pageUrl)).count())>0 ? true:false;
    }

    public static void getDbConfigs() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
         dbConfig = mapper.readValue(new File("src/main/resources/config/bdConfig.json"),
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class));
         mongoClient = new MongoClient((String) dbConfig.get(6), (Integer.parseInt((String) dbConfig.get(4))));
        db = mongoClient.getDB((String) dbConfig.get(2));
        coll = db.getCollection((String) dbConfig.get(8));

    }
}