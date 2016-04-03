package ifmo.escience.newscrawler.entities;

import ifmo.escience.newscrawler.ProxyManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RootEntity extends WebEntity {
    HtmlUnitDriver driver = new HtmlUnitDriver();
    public RootEntity(WebEntity from) throws IOException {
        this.newsListPath = from.newsListPath;
        this.entityName = from.entityName;
        this.entityUrl = from.entityUrl;

    }

    @Override
    public void run() {
        int syncDays = 60;
        StringBuffer addon = new StringBuffer();
        try {
            while (true) {
                LocalDateTime currentDate = LocalDateTime.now();
                for (int day = 0; day < syncDays; day++) {
                    LocalDateTime newDate = currentDate.minusDays(1);
                    for (int page = 0; page < 83; page++) {
                        addon.delete(0, addon.length());
                        addon.append(this.entityUrl);
                        addon.append("&from_day=" + String.valueOf(newDate.getDayOfMonth()));
                        addon.append("&from_month=" + String.valueOf(newDate.getMonthValue()));
                        addon.append("&from_year=" + String.valueOf(newDate.getYear()));
                        addon.append("&to_day=" + String.valueOf(currentDate.getDayOfMonth()));
                        addon.append("&to_month=" + String.valueOf(currentDate.getMonthValue()));
                        addon.append("&to_year=" + String.valueOf(currentDate.getYear()));
                        addon.append("&p=" + String.valueOf(page));
                        List<String> links = null;
                            do {
                                do {
                                    try {
                                        links = getLinks(addon.toString());
                                        crawler.addLinks(links);
                                    } catch (SocketTimeoutException exception) {
                                        System.out.println("BadProxy1");
                                    } catch (ConnectTimeoutException exception) {
                                        System.out.println("Bad proxy2");
                                    } catch (Exception e) {
                                        System.out.println("BadProxy3");
                                    }
                                } while (links == null);
                                System.out.println("оуч!");
                            }while (links.isEmpty());
                        System.out.println("Success");
                    }
                    currentDate = newDate;
                }
            }
        }

        catch(Exception ex)
        {
            logger.error("Error on collecting web pages!", ex);
        }

}
    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection huc = (HttpURLConnection)url.openConnection();
        huc.setRequestMethod("GET");
        huc.connect();
        return huc.getResponseCode();
    }

    @Override
    protected List<String> getLinks(String targetUrl) throws Exception {
        List<String> arrayOfWebPages = new ArrayList<>();
        logger.trace("Loading links from: " + targetUrl);
        Proxy proxy = new Proxy();

        proxy.setHttpProxy(ProxyManager.getProxy());
        driver.setProxySettings(proxy);
        driver.get(targetUrl);

        List<WebElement> links = driver.findElements(By.xpath(newsListPath));
        if (links.size()>0) {
            for (WebElement link : links) {
                String href = link.getAttribute("href");
                arrayOfWebPages.add(href);
            }
            crawler.addLinks(arrayOfWebPages);
        }
        return arrayOfWebPages;
    }

}
