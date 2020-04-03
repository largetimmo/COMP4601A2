package dao.modal;

public class Review {

    private String id;

    private String userId;

    private String pageId;

    private String content;

    private Integer helpful;

    private Integer helpless;

    private Double score;

    private String summary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getHelpful() {
        return helpful;
    }

    public void setHelpful(Integer helpful) {
        this.helpful = helpful;
    }

    public Integer getHelpless() {
        return helpless;
    }

    public void setHelpless(Integer helpless) {
        this.helpless = helpless;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
