package com.company;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import java.util.ArrayList;

/**
 * Created by gp on 21.10.15.
 */
//TODO for using mongo db see here:
//https://docs.mongodb.org/manual/tutorial/install-mongodb-on-ubuntu/

public class DBConnection {
    MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
    String[] titles = {"Number", "Title", "MainText", "Date", "Link", "MainLink"};

    public void putIntoDB(ArrayList<String> InfoIntoDB) {
        try{
            DB db = mongoClient.getDB( "mongo_database" );
            System.out.println("Connect to database successfully");
            DBCollection coll = db.getCollection("mycol");
            BasicDBObject doc = new BasicDBObject();
            for (int i =0; i<InfoIntoDB.size(); i++)
            {
                if (i<= titles.length-1) {
                    doc.append(titles[i], InfoIntoDB.get(i));
                }
                else{
                    doc.append("AdditionalTitle", InfoIntoDB.get(i));
                }
            }
            coll.insert(doc);

            System.out.println("Document inserted successfully");
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}