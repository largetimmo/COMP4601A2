package edu.carleton.comp4601.service;

import edu.carleton.comp4601.service.crawler.CrawlerManager;

public class StartUpManager {

    public StartUpManager() {
        startCrawler();
    }

    private void startCrawler() {
        CrawlerManager.getInstance();
    }
}
