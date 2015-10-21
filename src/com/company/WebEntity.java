package com.company;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WebEntity implements IEntity {

    private String entityName;
    private String entityUrl;
    private String newsListPath;
    private String articleNamePath;
    private String articleDatePath;
    private String articleTextPath;

    private long refreshTimeout;

    public String getArticleTextPath() {
        return articleTextPath;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityUrl() {
        return entityUrl;
    }

    public String getNewsListPath() {
        return newsListPath;
    }

    public String getArticleNamePath() {
        return articleNamePath;
    }

    public String getArticleDatePath() {
        return articleDatePath;
    }

    public long getRefreshTimeout() {
        return refreshTimeout;
    }




    public WebEntity entityFromCfg(String cfgPath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        WebEntity wEntity = mapper.readValue(new File(cfgPath), WebEntity.class);

        return wEntity;

    }

    public String entityToCfg(IEntity entity) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        File conf = new File("config/myObj.json");
        mapper.writeValue(conf, entity);

        return conf.getAbsolutePath();
    }

    @Override
    public String toString() {
        return (this.entityName
                + "\n" + this.entityUrl
                + "\n" + this.newsListPath);
    }

    public ArrayList<String> GetLinksFromTheMainSite(String MainLink) throws Exception {
        Document doc = Jsoup.connect(MainLink).get();
        Elements blockTitle = doc.select("li.article.switcher-all-news");///<<<!!!!!!!!!!
        Elements OnlyLinks = blockTitle.select("a[href]");         ///<<<!!!!!!!!!!
        ArrayList<String> ArrayOfLinks = new ArrayList<String>();
        for (int i = 0; i < OnlyLinks.size() - 1; i++) {
            if (((OnlyLinks.get(i)).attr("href").toString()).indexOf("http") == -1) {
                ArrayOfLinks.add(((OnlyLinks.get(i)).attr("href").toString()));//.replace("\'", ""));//href
            }
        }
        return ArrayOfLinks;
    }


    public ArrayList<String> ParsePage(String MainLink) throws Exception {
        Document doc1 = Jsoup.connect(MainLink).get();
        ArrayList<String> InfoAboutLink = new ArrayList<String>();

        Elements Header = doc1.select("h2.itemTitle");                //title for gaz = h1
        String Header_Text = Header.get(0).textNodes().toString();
        InfoAboutLink.add(Header_Text);
        //main header on main page
        Elements blockTitle1 = doc1.select("div.itemFullText"); // MainText gaz = div#ntext  Font = div.itemFullText
        String s = (blockTitle1.get(0).text()).toString(); //main text
        //add text correct between transger into string
        InfoAboutLink.add(s);
        //time on main page
        Elements Time = doc1.select("span.itemDateCreated");                       //Time span.itemDateCreated for F i for gaz
        String Time_Text = Time.get(0).textNodes().toString().replace("&nbsp;", " ");
        InfoAboutLink.add(Time_Text);
        return InfoAboutLink;
    }

    public void parse() throws Exception {

        ArrayList<String> LinksFromMainSite = new ArrayList<String>();
        ArrayList<String> ArrayLinksFromTheMainSite = new ArrayList<String>();
        ArrayLinksFromTheMainSite = GetLinksFromTheMainSite(entityUrl);
        DBConnection mySqlConection = new DBConnection();
        for (int i = 0; i < ArrayLinksFromTheMainSite.size(); i++) {

            {
                ArrayList<String> ArrayInfoIntoDB = new ArrayList<String>();
                LinksFromMainSite.add(entityUrl.substring(0, entityUrl.length() - 1) + ArrayLinksFromTheMainSite.get(i));//Here 9 1
                ArrayInfoIntoDB = ParsePage(LinksFromMainSite.get(i));
                ArrayInfoIntoDB.add(0, Integer.toString(i));
                ArrayInfoIntoDB.add(LinksFromMainSite.get(i));
                ArrayInfoIntoDB.add(entityUrl);
                mySqlConection.PutIntoDB(ArrayInfoIntoDB);
            }
        }

        System.out.println("Total number of news in the table : " + mySqlConection.ShowNumOfNewsInDB());
    }

    public static void main(String[] args) throws IOException {
        WebEntity we = new WebEntity();
        we = we.entityFromCfg("config/config.json");
        System.out.println(we.toString());
    }
}
