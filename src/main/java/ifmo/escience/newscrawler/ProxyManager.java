package ifmo.escience.newscrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gp on 30.03.16.
 */
public class ProxyManager {
    public static List<String> proxyList = new ArrayList<>();
    public ProxyManager() throws IOException {
        proxyList =  updateList();
    }

    List updateList() throws IOException {

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.ifmo.ru", 3128));
        HttpURLConnection connection = (HttpURLConnection)new URL("http://txt.proxyspy.net/proxy.txt").openConnection(proxy);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            if (line.contains("+")) {
                Matcher m = Pattern.compile("\\d+.\\d+.\\d+.\\d+:\\d+ ").matcher(line);
                while (m.find()) {
                    proxyList.add(m.group().trim());
                }
            }
        }
        in.close();
    return proxyList;
    }

    public String getProxy() throws IOException {
        if (proxyList.size()==0)
            proxyList = updateList();
        String link = proxyList.get(0);
        proxyList.remove(0);
        return  link;
        }

}
