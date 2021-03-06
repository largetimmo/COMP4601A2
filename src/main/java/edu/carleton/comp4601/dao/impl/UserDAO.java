package edu.carleton.comp4601.dao.impl;

import com.mongodb.client.model.Filters;
import edu.carleton.comp4601.dao.AbstractDAO;
import edu.carleton.comp4601.dao.MongoDAO;
import edu.carleton.comp4601.dao.modal.User;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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

    public User getById(String id){
        return map((Document) collection.find(Filters.eq("userId",id)).iterator().tryNext());
    }
    public void update(User user){
        collection.replaceOne(Filters.eq("userId",user.getId()),map(user));
    }

    public List<User> findAllUsers(){
        List<User> userList = new LinkedList<>();
        collection.find().forEach((Consumer<? super Document>) d->{
            userList.add(map(d));
        });
        return userList;
    }

    @Override
    public Document map(User entity) {
        Document document = new Document();
        document.put("userId",entity.getId());
        document.put("viewedSite",entity.getViewedSite());
        document.put("reviews",entity.getReviews());
        document.put("scoreAvg",entity.getScoreAvg());
        document.put("helpful",entity.getHelpful());
        document.put("thumbsFromOthers",entity.getThumbsFromOthers());
        document.put("veryPositive",entity.getVeryPositive());
        document.put("positive",entity.getPositive());
        document.put("natural",entity.getNatural());
        document.put("negative",entity.getNegative());
        document.put("veryNegative",entity.getVeryNegative());
        return document;
    }

    @Override
    public User map(Document document) {
        if(document == null){
            return  null;
        }
        User user = new User();
        user.setId(document.get("userId").toString());
        if(document.get("viewedSite") !=null){
            user.setViewedSite(document.getList("viewedSite",String.class));
        }
        if(document.get("reviews")!=null){
            user.setReviews(document.getList("reviews",String.class));
        }
        if(document.get("scoreAvg") != null){
            user.setScoreAvg(document.getDouble("scoreAvg"));
        }
        if(document.get("helpful")!=null){
            user.setHelpful(document.getDouble("helpful"));
        }
        if(document.get("thumbsFromOthers")!=null){
            user.setThumbsFromOthers(document.getInteger("thumbsFromOthers"));
        }
        user.setVeryPositive(document.getInteger("veryPositive"));
        user.setPositive(document.getInteger("positive"));
        user.setNatural(document.getInteger("natural"));
        user.setNegative(document.getInteger("negative"));
        user.setVeryNegative(document.getInteger("veryNegative"));


        return user;
    }
}
