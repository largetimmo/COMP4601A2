package dao.modal;

import java.util.List;
import java.util.Map;

public class CrawlDataEntity {

    private Integer id;

    private List<String> content;

    private String url;

    private List<Integer> childId;

    private List<String> childUrl;

    private List<Integer> parentUrl;

    private String type;

    public CrawlDataEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Integer> getChildId() {
        return childId;
    }

    public void setChildId(List<Integer> childId) {
        this.childId = childId;
    }

    public List<String> getChildUrl() {
        return childUrl;
    }

    public void setChildUrl(List<String> childUrl) {
        this.childUrl = childUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(List<Integer> parentUrl) {
        this.parentUrl = parentUrl;
    }
}
