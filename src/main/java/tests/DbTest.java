package tests;

import ifmo.escience.newscrawler.WebPage;
import ifmo.escience.newscrawler.database.DbConfig;
import ifmo.escience.newscrawler.database.NewsMongoDb;
import junit.framework.TestCase;
import junit.framework.TestResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by JuriaSan on 13.07.2016.
 */
public class DbTest extends TestCase {

    NewsMongoDb db;
    public DbTest() {
        db = new NewsMongoDb(DbConfig.dbConfigsFromProperties("config.properties"));
        try {
            db.connect();
        }
        catch(Exception e) {

        }

    }

    @Test
    public void testExitsting() {

        WebPage p = new WebPage("http://blablabla11111.com");
        db.insert(p);
        boolean result =  db.urlExists("http://blablabla11111.com");
        Assert.assertTrue(result);
        db.remove(p);
    }
    @Test
    public void NontestExitsting() {

        WebPage p = new WebPage("http://blablabla11111.com");
        db.insert(p);
        boolean result =  db.urlExists("http://blablabla2222.com");
        Assert.assertFalse(result);
        db.remove(p);
    }
}
