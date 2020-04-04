package service;

import dao.impl.UserDAO;
import service.crawler.CrawlerManager;

import java.io.IOException;

public class StartUpManager {

    public StartUpManager() {
        startCrawler();
    }

    private void startCrawler() {
       // CrawlerManager.getInstance();
    }
}
