package dao.modal;

import java.util.List;

public class User {

    private String id;

    private List<String> viewedSite;

    private List<String> reviews;

    private Double scoreAvg;

    private Double helpful;

    private Integer thumbsFromOthers;

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
}
