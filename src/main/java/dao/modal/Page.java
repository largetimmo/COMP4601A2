package dao.modal;

import java.util.List;

public class Page {

    private String id;

    private List<String> reviewsIds;

    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getReviewsIds() {
        return reviewsIds;
    }

    public void setReviewsIds(List<String> reviewsIds) {
        this.reviewsIds = reviewsIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
