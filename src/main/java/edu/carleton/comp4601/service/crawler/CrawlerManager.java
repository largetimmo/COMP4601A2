package edu.carleton.comp4601.service.crawler;

import edu.carleton.comp4601.dao.impl.PageDAO;
import edu.carleton.comp4601.dao.impl.ReviewDAO;
import edu.carleton.comp4601.dao.impl.UserDAO;
import edu.carleton.comp4601.dao.modal.Page;
import edu.carleton.comp4601.dao.modal.Review;
import edu.carleton.comp4601.dao.modal.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CrawlerManager {

    private static final String REVIEW_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/reviews.zip ";
    private static final String USER_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/users.zip";
    private static final String PAGE_URL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/pages.zip";
    private static final String SENTIMENT_IDV1 = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/sentiment-reviews-individual.csv";
    private static final String SENTIMENT_IDV2 = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/archive/sentiment-reviews-individual2.csv";

    private static final CrawlerManager INSTANCE = new CrawlerManager();

    private UserDAO userDAO = UserDAO.getInstance();

    private PageDAO pageDAO = PageDAO.getInstance();

    private ReviewDAO reviewDAO = ReviewDAO.getInstance();


    public CrawlerManager() {
        loadUser();
        loadPages();
        loadReviews();
        calculateUserProfile();
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
                    if (count == -1) {
                        continue;
                    }
                    sb.append(new String(Arrays.copyOf(buffer, count)));
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
                    if (count == -1) {
                        continue;
                    }
                    sb.append(new String(Arrays.copyOf(buffer, count)));
                }
                Document jsoup = Jsoup.parse(sb.toString());
                List<Element> links = jsoup.body().getElementsByTag("a");
                List<String> reviews = links.stream()
                        .map(l -> l.attr("href"))
                        .filter(l -> l.endsWith(".html"))
                        .map(s -> s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf(".")))
                        .map(s -> s + "-" + page.getId())
                        .collect(Collectors.toList());
                page.setReviewsIds(reviews);
                page.setContent(jsoup.body().toString());
                pageDAO.save(page);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void loadReviews() {
        byte[] buffer = new byte[102400];
        Map<String, String> sentiment = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(SENTIMENT_IDV1).openStream()));
            List<String> lines = br.lines().filter(s -> !s.isEmpty()).collect(Collectors.toList());
            IntStream.range(1, lines.size()).forEach(i -> sentiment.put(lines.get(i).substring(0, lines.get(i).indexOf(",")), lines.get(i).substring(lines.get(i).indexOf(",") + 1)));
            br.close();
            br = new BufferedReader(new InputStreamReader(new URL(SENTIMENT_IDV2).openStream()));
            List<String> lines2 = br.lines().filter(s -> !s.isEmpty()).collect(Collectors.toList());
            IntStream.range(1, lines2.size()).forEach(i -> sentiment.put(lines2.get(i).substring(0, lines2.get(i).indexOf(",")), lines2.get(i).substring(lines2.get(i).indexOf(",") + 1)));
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadZipFile(REVIEW_URL, (ZipInputStream zis, ZipEntry entry) -> {
            Review review = new Review();
            review.setId(entry.getName().substring(entry.getName().lastIndexOf("/") + 1, entry.getName().indexOf(".html")));
            try {
                StringBuilder sb = new StringBuilder();
                int count = 0;
                while (zis.available() > 0) {
                    count = zis.read(buffer);
                    if (count == -1) {
                        continue;
                    }
                    sb.append(new String(Arrays.copyOf(buffer, count)));
                }
                Document jsoup = Jsoup.parse(sb.toString());
                String filename = entry.getName();
                String userId = filename.substring(filename.lastIndexOf("/") + 1, filename.lastIndexOf("-"));
                String pageId = filename.substring(filename.lastIndexOf("-") + 1, filename.lastIndexOf(".html"));
                User user = userDAO.getById(userId);
                if (user.getReviews() == null) {
                    user.setReviews(new ArrayList<>());
                }
                user.getReviews().add(review.getId());

                review.setContent(jsoup.body().text());
                review.setUserId(userId);
                review.setPageId(pageId);
                Map<String, String> metadata = jsoup.head().getElementsByTag("meta").stream().map(e -> new Map.Entry<String, String>() {
                    @Override
                    public String getKey() {
                        return e.attr("name");
                    }

                    @Override
                    public String getValue() {
                        return e.attr("content");
                    }

                    @Override
                    public String setValue(String value) {
                        return null;
                    }
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                review.setSummary(metadata.get("summary"));
                review.setScore(Double.parseDouble(metadata.get("score")));
                int helpful = Integer.parseInt(metadata.get("helpfulness").substring(0, metadata.get("helpfulness").lastIndexOf("/")));
                int helpless = Integer.parseInt(metadata.get("helpfulness").substring(metadata.get("helpfulness").lastIndexOf("/") + 1));
                review.setHelpful(helpful);
                review.setHelpless(helpless);
                String sentimentReview = sentiment.get(review.getId() + ".html");
                if (sentimentReview == null || sentimentReview.isEmpty()) {
                    System.out.println("Missing sentiment for :" + review.getId());
                    sentimentReview = "0,0,0,0,0";

                }
                String[] sentimentArray = sentimentReview.split(",");
                review.setVeryPositive(Integer.parseInt(sentimentArray[0]));
                review.setPositive(Integer.parseInt(sentimentArray[1]));
                review.setNatural(Integer.parseInt(sentimentArray[2]));
                review.setNegative(Integer.parseInt(sentimentArray[3]));
                review.setVeryNegative(Integer.parseInt(sentimentArray[4]));
                if (user.getVeryPositive() == null) {
                    user.setVeryPositive(0);
                }
                user.setVeryPositive(user.getVeryPositive() + review.getVeryPositive());
                if (user.getPositive() == null) {
                    user.setPositive(0);
                }
                user.setPositive(user.getPositive() + review.getPositive());
                if (user.getNatural() == null) {
                    user.setNatural(0);
                }
                user.setNatural(user.getNatural() + review.getNatural());
                if (user.getNegative() == null) {
                    user.setNegative(0);
                }
                user.setNegative(user.getNegative() + review.getNegative());
                if (user.getVeryNegative() == null) {
                    user.setVeryNegative(0);
                }
                user.setVeryNegative(user.getVeryNegative() + review.getVeryNegative());

                userDAO.update(user);
                reviewDAO.save(review);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void calculateUserProfile() {
        List<User> userList = userDAO.findAllUsers();
        for (User u : userList) {
            List<Review> reviews = reviewDAO.findByUserId(u.getId());
            u.setThumbsFromOthers(reviews.stream().mapToInt(r -> r.getHelpful() + r.getHelpless()).sum());
            u.setHelpful(((double) reviews.stream().mapToInt(Review::getHelpful).sum()) / u.getThumbsFromOthers());
            u.setScoreAvg(reviews.stream().mapToDouble(Review::getScore).sum() / u.getReviews().size());
            userDAO.update(u);
        }
    }

    public static void main(String[] args) {
        CrawlerManager crawlerManager = new CrawlerManager();
        crawlerManager.loadReviews();
    }

}

