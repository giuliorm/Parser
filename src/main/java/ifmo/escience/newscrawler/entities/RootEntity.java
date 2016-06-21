package ifmo.escience.newscrawler.entities;

import ifmo.escience.newscrawler.ProxyManager;
import ifmo.escience.newscrawler.Utils;
import ifmo.escience.newscrawler.database.NewsMongoDb;
import org.apache.http.conn.ConnectTimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RootEntity extends WebEntity {

    Random rand = new Random();
    HtmlUnitDriver driver = new HtmlUnitDriver();
    protected HashMap<String, WebEntity> existingEntities;

    public RootEntity() {

    }

    public RootEntity(WebEntity from,
                      NewsMongoDb connection,
                      HashMap<String,WebEntity> existingEntities)
            throws IOException {

        super(null, from.entityUrl, connection);
        this.newsListPath = from.newsListPath;
        this.entityName = from.entityName;
        this.entityUrl = from.entityUrl;
        this.existingEntities = existingEntities;
    }

    public HashMap<String, WebEntity> getExistingEntities() {
        return this.existingEntities;
    }

    private void parseLinks(List<String> links) {

        if (links.size() < 1)
            return;

        ExecutorService service  =
                Executors.newFixedThreadPool(
                        Utils.threadPoolCount(links.size()));

        for (String link : links) {
            String key = Utils.getUrlStd(link);
            if (this.existingEntities.containsKey(key)) {

                WebEntity entity = this.existingEntities.get(key);
                service.execute(new WebEntity(entity, link, connection));

            } else System.out.println("Key is not valid for entity " + link);
        }
        service.shutdown();
        while(!service.isTerminated()) {

        }
    }

    private String buildUrl(LocalDateTime newDate, LocalDateTime currentDate, int pageNumber) {
        StringBuffer addon = new StringBuffer();

        addon.append(this.entityUrl);
        addon.append("&from_day=" + String.valueOf(newDate.getDayOfMonth()));
        addon.append("&from_month=" + String.valueOf(newDate.getMonthValue()));
        addon.append("&from_year=" + String.valueOf(newDate.getYear()));
        addon.append("&to_day=" + String.valueOf(currentDate.getDayOfMonth()));
        addon.append("&to_month=" + String.valueOf(currentDate.getMonthValue()));
        addon.append("&to_year=" + String.valueOf(currentDate.getYear()));
        addon.append("&p=" + String.valueOf(pageNumber));

        return addon.toString();
    }


    private List<String> loadLinks (String url) {

        List<String> links;

        do {
            links = tryLoadLinks(url);
        }
        while(links == null || links != null && links.isEmpty());

        return links;
    }

    private List<String> tryLoadLinks(String url) {

        List<String> links = null;

        try {

            /*
                Thread.sleep is necessary for avoiding server's 'Your queries look like automatic' responses.
                If we increase query time gap, server will return correct answer.
             */

            Thread.sleep(2_000);
            links = getLinks(url);
        }
        catch (SocketTimeoutException exception) {

            System.out.println("BadProxy1");

        } catch (ConnectTimeoutException exception) {

            System.out.println("Bad proxy2");

        } catch (Exception e) {

            System.out.println("BadProxy3");
        }

        return links;
    }

    @Override
    public void run() {

        System.out.println("Root thread #" + Thread.currentThread().getName() + " is started");
        int max = 25;
        int syncDays = 60;
        int  time = 45_000 + rand.nextInt(30_000) + 1;

        try {
            while (true) {

                LocalDateTime currentDate = LocalDateTime.now();

                for (int day = 0; day < syncDays; day++) {

                    LocalDateTime newDate = currentDate.minusDays(1);

                    for (int page = 0; page < 83; page++) {

                        String url = buildUrl(newDate, currentDate, page);

                        List<String> links = loadLinks(url);

                        parseLinks(links);

                        /*
                           Thread.sleep is necessary for avoiding server's 'Your queries look like automatic' responses.
                           If we increase query time gap, server will return correct answer.
                        */
                        Thread.sleep(time);
                    }
                    currentDate = newDate;
                }
              //  max --;
            }
        }

        catch(Exception ex)
        {
            logger.error("Error on collecting web pages!", ex);
        }

        System.out.println("Root thread #" + Thread.currentThread().getName() + " is exiting");

    }


    @Override
    protected List<String> getLinks(String targetUrl) throws Exception {

        List<String> arrayOfWebPages = new ArrayList<>();

        logger.trace("Loading links from: " + targetUrl);

        Proxy proxy = new Proxy();
        String proxyFromSet = ProxyManager.getProxy();
        proxy.setSslProxy(proxyFromSet);
        driver.setProxySettings(proxy);

        driver.get(targetUrl);

       // String body = driver.findElement(By.tagName("body")).getText();

        List<String> links = driver.findElements(By.xpath(newsListPath))
                .stream()
                .map(link -> link.getAttribute("href"))
                .collect(Collectors.toList());
                //.forEach(ProxyManager::feedBack(proxyFromSet));

        if (links.size() > 0)
            ProxyManager.feedBack(proxyFromSet);

       /* if (links.size()>0) {
            for (WebElement link : links) {
                String href = link.getAttribute("href");
                arrayOfWebPages.add(href);
            }
            ProxyManager.feedBack(proxyFromSet);
            //crawler.addLinks(arrayOfWebPages);
        }
        return arrayOfWebPages; */
        return links;
    }
}
