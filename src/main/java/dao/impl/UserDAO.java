package dao.impl;

import dao.AbstractDAO;
import dao.MongoDAO;
import dao.modal.User;
import org.bson.Document;

public class UserDAO extends AbstractDAO implements MongoDAO<User> {

    private static final UserDAO INSTANCE = new UserDAO();
    public static UserDAO getInstance(){
        return INSTANCE;
    }

    public void save(User user){
        collection.insertOne(map(user));
    }

    public UserDAO() {
        super("user");
    }

    @Override
    public Document map(User entity) {
        Document document = new Document();
        document.put("id",entity.getId());
        document.put("viewedSite",entity.getViewedSite());
        document.put("reviews",entity.getReviews());
        return document;
    }

    @Override
    public User map(Document document) {
        if(document == null){
            return  null;
        }
        User user = new User();
        user.setId(document.get("id").toString());
        if(document.get("viewedSite") !=null){
            user.setViewedSite(document.getList("viewedSite",String.class));
        }
        if(document.get("reviews")!=null){
            user.setReviews(document.getList("reviews",String.class));
        }

        return user;
    }
}
