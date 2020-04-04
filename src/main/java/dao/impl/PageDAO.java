package dao.impl;

import com.mongodb.client.model.Filters;
import dao.AbstractDAO;
import dao.MongoDAO;
import dao.modal.Page;
import dao.modal.User;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class PageDAO extends AbstractDAO implements MongoDAO<Page> {
    private static final PageDAO INSTANCE = new PageDAO();
    public static PageDAO getInstance(){
        return INSTANCE;
    }

    public PageDAO() {
        super("page");
    }

    public void save(Page page){
        collection.insertOne(map(page));
    }

    public Page getById(String pageId){
        return map(collection.find(Filters.eq("id", pageId)).iterator().tryNext());
    }

    public List<Page> findAllPages(){
        List<Page> pages = new LinkedList<>();
        collection.find().forEach((Consumer<? super Document>) d->{
            pages.add(map(d));
        });
        return pages;
    }

    @Override
    public Document map(Page entity) {
        Document document = new Document();
        document.put("id",entity.getId());
        document.put("reviewsIds",entity.getReviewsIds());
        document.put("content",entity.getContent());
        return document;
    }

    @Override
    public Page map(Document document) {
        if(document == null){
            return null;
        }
        Page page = new Page();
        if(document.get("id")!=null){
            page.setId(document.get("id").toString());
        }
        if(document.get("reviewsIds")!=null){
            page.setReviewsIds(document.getList("reviewsIds",String.class));
        }
        page.setContent(document.getString("content"));
        return page;
    }
}
