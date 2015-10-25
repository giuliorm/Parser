package com.company;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebEntity implements IEntity {
/*
    private String entityName = "site";
    public String entityUrl = "http://m.fontanka.ru/";
    private String newsListPath = "li.article.switcher-all-news";
    private String articleNamePath = "h2.itemTitle";
    private String articleDatePath = "span.itemDateCreated";
    private String articleTextPath = "div.itemFullText";
    */


    private String entityName = "site";
    public String entityUrl = "http://www.gazeta.spb.ru/allnews/";
    private String newsListPath = "div.materials.nonLine";
    private String articleNamePath = "h1";
    private String articleDatePath = "i";
    private String articleTextPath = "div#ntext";

/*{
  "entityName": "fontanka",
  "entityUrl": "http://m.fontanka.ru/",
  "newsListPath": "li.article.switcher-all-news",
  "articleNamePath": "h2.itemTitle",
  "articleDatePath": "span.itemDateCreated",
  "articleTextPath": "div.itemFullText",
  "refreshTimeout": "600000"
}
*/


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


    //Single-configuration file
    public WebEntity entityFromCfg(String cfgPath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        WebEntity wEntity = mapper.readValue(new File(cfgPath), WebEntity.class);

        return wEntity;

    }

    //Able to load multi-configuration files.
    public ArrayList<WebEntity> entityListFromCfg(String cfgPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ArrayList<WebEntity> webEntityList = mapper.readValue(new File(cfgPath),
                mapper.getTypeFactory().constructCollectionType(ArrayList.class, WebEntity.class));

        return webEntityList;
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
        Document doc = Jsoup.connect(entityUrl).get();
        Elements blockTitle = doc.select(newsListPath);///<<<!!!!!!!!!!
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

        Elements Header = doc1.select(articleNamePath);                //title for gaz = h1
        String Header_Text = Header.get(0).textNodes().toString();
        InfoAboutLink.add(Header_Text);
        //main header on main page
        Elements blockTitle1 = doc1.select(articleTextPath); // MainText gaz = div#ntext  Font = div.itemFullText
        String s = (blockTitle1).toString();
        //String s = (blockTitle1.get(0).text()).toString(); //main text
        //add text correct between transger into string
        InfoAboutLink.add(s);
        //time on main page
        Elements Time = doc1.select(articleDatePath);                       //Time span.itemDateCreated for F i for gaz
        String Time_Text = Time.get(0).textNodes().toString().replace("&nbsp;", " ");
        InfoAboutLink.add(Time_Text);
        return InfoAboutLink;
    }

    public void parse() throws Exception {

        ArrayList<String> LinksFromMainSite = new ArrayList<String>();
        ArrayList<String> ArrayLinksFromTheMainSite = new ArrayList<String>();
        ArrayLinksFromTheMainSite = GetLinksFromTheMainSite(entityUrl);
        DBConnection mySqlConection = new DBConnection();
        entityUrl = (regExp(entityUrl));
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
        //we = we.entityFromCfg("config/config.json");
        //System.out.println(we.toString());
        we.entityListFromCfg("config/multiConfig.json");
    }

    public String regExp(String entityUrl) throws Exception {
        String LinkAfterRegExp = null;
        Pattern urlPattern = Pattern.compile("^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})\\/");
        Matcher urlMatcher = urlPattern.matcher(entityUrl);
        while (urlMatcher.find()) {
            LinkAfterRegExp = (entityUrl.substring(urlMatcher.start(), urlMatcher.end()) + "");
        }
        return LinkAfterRegExp;
    }
}