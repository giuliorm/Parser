package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DBConnection {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    DB db = mongoClient.getDB( "mongo_database" );
    DBCollection coll = db.getCollection("mycol");

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
}