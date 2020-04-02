package dao.modal;

import java.util.List;

public class User {

    private String id;

    private List<String> viewedSite;

    private List<String> reviews;

    private Double score;

    private Double helpful;

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
}
