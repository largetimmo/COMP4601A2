package edu.carleton.comp4601.dao.impl;

import com.mongodb.client.model.Filters;
import edu.carleton.comp4601.dao.AbstractDAO;
import edu.carleton.comp4601.dao.MongoDAO;
import edu.carleton.comp4601.dao.modal.Review;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ReviewDAO extends AbstractDAO implements MongoDAO<Review> {
    private static final ReviewDAO INSTANCE = new ReviewDAO();
    public static ReviewDAO getInstance(){
        return INSTANCE;
    }

    public ReviewDAO() {
        super("review");
    }

    public void save(Review review){
        collection.insertOne(map(review));
    }

    public List<Review> findByUserId(String userId){
        List<Review> result = new ArrayList<>();
        collection.find(Filters.eq("userId",userId)).forEach((Consumer<? super Document>) d-> result.add(map(d)));
        return result;
    }
    public Review getById(String id){
        return map(collection.find(Filters.eq("id",id)).iterator().tryNext());
    }

    @Override
    public Document map(Review entity) {
        Document document = new Document();
        document.put("id",entity.getId());
        document.put("userId",entity.getUserId());
        document.put("pageId",entity.getPageId());
        document.put("content",entity.getContent());
        document.put("helpful",entity.getHelpful());
        document.put("helpless",entity.getHelpless());
        document.put("score",entity.getScore());
        document.put("summary",entity.getSummary());
        document.put("veryPositive",entity.getVeryPositive());
        document.put("positive",entity.getPositive());
        document.put("natural",entity.getNatural());
        document.put("negative",entity.getNegative());
        document.put("veryNegative",entity.getVeryNegative());
        return document;
    }

    @Override
    public Review map(Document document) {
        if(document == null){
            return null;
        }
        Review review = new Review();
        review.setId(document.getString("id"));
        review.setUserId(document.getString("userId"));
        review.setPageId(document.getString("pageId"));
        review.setContent(document.getString("content"));
        review.setHelpful(document.getInteger("helpful"));
        review.setHelpless(document.getInteger("helpless"));
        review.setScore(document.getDouble("score"));
        review.setSummary(document.getString("summary"));
        review.setVeryPositive(document.getInteger("veryPositive"));
        review.setPositive(document.getInteger("positive"));
        review.setNatural(document.getInteger("natural"));
        review.setNegative(document.getInteger("negative"));
        review.setVeryNegative(document.getInteger("veryNegative"));
        return review;
    }
}
