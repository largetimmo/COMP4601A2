package dao.impl;

import dao.AbstractDAO;
import dao.MongoDAO;
import dao.modal.Page;
import org.bson.Document;

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

    @Override
    public Document map(Page entity) {
        Document document = new Document();
        document.put("id",entity.getId());
        document.put("reviewsIds",entity.getReviewsIds());
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
        return page;
    }
}
