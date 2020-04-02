package service.crawler;

import dao.impl.PageDAO;
import dao.impl.UserDAO;
import dao.modal.Page;
import dao.modal.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CrawlerManager {

    private static final String REVIEW_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/reviews.zip ";
    private static final String USER_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/users.zip";
    private static final String PAGE_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/pages.zip";

    private static final CrawlerManager INSTANCE = new CrawlerManager();

    private UserDAO userDAO = UserDAO.getInstance();

    private PageDAO pageDAO = PageDAO.getInstance();


    public CrawlerManager() {
        loadUser();
        loadPages();
        loadReviews();
    }

    private int id = 0;

    public static CrawlerManager getInstance() {
        return INSTANCE;
    }

    private void loadZipFile(String url, BiConsumer<ZipInputStream, ZipEntry> consumer) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new URL(url).openStream());
            for (ZipEntry zipEntry = zipInputStream.getNextEntry(); ; zipEntry = zipInputStream.getNextEntry()) {
                if (zipEntry == null) {
                    break;
                }
                if (!zipEntry.getName().endsWith("html")) {
                    continue;
                }
                consumer.accept(zipInputStream, zipEntry);
            }
            zipInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUser() {
        byte[] buffer = new byte[102400];
        loadZipFile(USER_URL, (ZipInputStream zis, ZipEntry entry) -> {
            User user = new User();
            user.setId(entry.getName().substring(entry.getName().lastIndexOf("/") + 1, entry.getName().indexOf(".html")));
            try {
                StringBuilder sb = new StringBuilder();
                int count = 0;
                while (zis.available() > 0) {
                    count = zis.read(buffer);
                    if(count == -1){
                        continue;
                    }
                    sb.append(new String(Arrays.copyOf(buffer,count)));
                }
                Document jsoup = Jsoup.parse(sb.toString());
                List<Element> links = jsoup.body().getElementsByTag("a");
                List<String> pages = links.stream().map(l -> l.attr("href")).map(s -> s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."))).collect(Collectors.toList());
                user.setViewedSite(pages);
                userDAO.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void loadPages() {
        byte[] buffer = new byte[102400];
        loadZipFile(PAGE_URL, (ZipInputStream zis, ZipEntry entry) -> {
            Page page = new Page();
            page.setId(entry.getName().substring(entry.getName().lastIndexOf("/") + 1, entry.getName().indexOf(".html")));
            try {
                StringBuilder sb = new StringBuilder();
                int count = 0;
                while (zis.available() > 0) {
                    count = zis.read(buffer);
                    if(count == -1){
                        continue;
                    }
                    sb.append(new String(Arrays.copyOf(buffer,count)));
                }
                Document jsoup = Jsoup.parse(sb.toString());
                List<Element> links = jsoup.body().getElementsByTag("a");
                List<String> reviews = links.stream()
                        .map(l -> l.attr("href"))
                        .filter(l->l.endsWith(".html"))
                        .map(s -> s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf(".")))
                        .map(s->s+"-"+page.getId())
                        .collect(Collectors.toList());
                page.setReviewsIds(reviews);
                pageDAO.save(page);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void loadReviews() {

    }

}

