package edu.carleton.comp4601.dao.modal;

import java.util.List;

public class User {

    private String id;

    private List<String> viewedSite;

    private List<String> reviews;

    private Double scoreAvg;

    private Double helpful;

    private Integer thumbsFromOthers;

    private Integer veryPositive;

    private Integer positive;

    private Integer natural;

    private Integer negative;

    private Integer veryNegative;

    public Integer getThumbsFromOthers() {
        return thumbsFromOthers;
    }

    public void setThumbsFromOthers(Integer thumbsFromOthers) {
        this.thumbsFromOthers = thumbsFromOthers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getViewedSite() {
        return viewedSite;
    }

    public void setViewedSite(List<String> viewedSite) {
        this.viewedSite = viewedSite;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }

    public Double getScoreAvg() {
        return scoreAvg;
    }

    public void setScoreAvg(Double scoreAvg) {
        this.scoreAvg = scoreAvg;
    }

    public Double getHelpful() {
        return helpful;
    }

    public void setHelpful(Double helpful) {
        this.helpful = helpful;
    }

    public Integer getVeryPositive() {
        return veryPositive;
    }

    public void setVeryPositive(Integer veryPositive) {
        this.veryPositive = veryPositive;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    public Integer getNatural() {
        return natural;
    }

    public void setNatural(Integer natural) {
        this.natural = natural;
    }

    public Integer getNegative() {
        return negative;
    }

    public void setNegative(Integer negative) {
        this.negative = negative;
    }

    public Integer getVeryNegative() {
        return veryNegative;
    }

    public void setVeryNegative(Integer veryNegative) {
        this.veryNegative = veryNegative;
    }
}
