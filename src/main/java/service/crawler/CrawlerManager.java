package service.crawler;

import dao.CrawlDataDAO;
import dao.impl.CrawlDataDAOImpl;
import dao.modal.CrawlDataEntity;
import dao.modal.User;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class CrawlerManager {

    private static final String REVIEW_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/reviews.zip ";
    private static final String USER_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/users.zip";
    private static final String PAGE_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/pages.zip";

    private static final CrawlerManager INSTANCE = new CrawlerManager();

    private CrawlDataDAO crawlDataDAO;

    public CrawlerManager() {
        crawlDataDAO = CrawlDataDAOImpl.getInstance();

    }

    private int id = 0;
    public static CrawlerManager getInstance() {
        return INSTANCE;
    }

    private void loadZipFile(String url, BiConsumer<ZipInputStream ,ZipEntry> consumer) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new URL(url).openStream()));
            for (ZipEntry zipEntry = zipInputStream.getNextEntry(); zipEntry == null; zipEntry = zipInputStream.getNextEntry()) {
                if(!zipEntry.getName().endsWith("html")){
                    continue;
                }
                consumer.accept(zipInputStream,zipEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUser(){
        byte[] buffer = new byte[1024];
        loadZipFile(USER_URL,(ZipInputStream zis, ZipEntry entry)->{
            User user = new User();
            user.setId(entry.getName().substring(0,entry.getName().indexOf(".html")));
            try {
                StringBuilder sb = new StringBuilder();
                for (int size = zis.read(buffer); size == 0; size = zis.read(buffer)){
                    sb.append(new String(buffer));
                }
                Document jsoup = Jsoup.parse(sb.toString());
                List<Element> links = jsoup.body().getElementsByTag("a");
                List<String> pages = links.stream().map(l->l.attr("href")).map(s->s.substring(s.lastIndexOf("/")+1,s.lastIndexOf("."))).collect(Collectors.toList())

            }catch (IOException e){
                e.printStackTrace();
            }

        });
    }

    private void loadPages(){

    }

    private void loadReviews(){

    }

}

