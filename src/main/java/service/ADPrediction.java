package service;


import dao.Community.CommunityCalculator;
import dao.impl.PageDAO;
import dao.impl.ReviewDAO;
import dao.impl.UserDAO;
import dao.modal.Review;
import dao.modal.User;

import java.io.IOException;
import java.util.*;

public class ADPrediction {
    private UserDAO userDAO = UserDAO.getInstance();
    private PageDAO pageDAO = PageDAO.getInstance();
    private ReviewDAO reviewDao = ReviewDAO.getInstance();
    private CommunityCalculator communityCalculator;
    private ArrayList<String> resultPageIds;
    private ArrayList<Double> resultPagesScores;

    public ADPrediction(int optimalk){
        List<User> users = userDAO.findAllUsers();
        communityCalculator = new CommunityCalculator(users.size(),optimalk);
        for (User u:users){
            communityCalculator.addUser(u);
            System.out.println(u.getId());
        }
        communityCalculator.algorithm();
        resultPageIds = new ArrayList<>();
        resultPagesScores = new ArrayList<>();
    }

    public ArrayList<Double> getResultPagesScores() {
        return resultPagesScores;
    }

    public ArrayList<String> getResultPageIds() {
        return resultPageIds;
    }

    public void CalculateAD(int cluster){
        ArrayList<User> users = communityCalculator.getAllUsersInClusterById(cluster);
        ArrayList<Review> reviews = new ArrayList<>();
        for (User u: users){
            reviews.addAll(reviewDao.findByUserId(u.getId()));
        }
        Set<String> pageIds = new HashSet<>();
        for (Review r: reviews){
            pageIds.add(r.getPageId());
        }
        Map<String,Double> pageScore = new LinkedHashMap<>();
        for (String s:pageIds){
            pageScore.put(s,0.0);
        }
        for (Review r: reviews){
            Double tempScore = pageScore.get(r.getPageId());
            pageScore.put(r.getPageId(),tempScore+r.getScore());
        }

        Object[] a = pageScore.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Double>) o2).getValue()
                        .compareTo(((Map.Entry<String, Double>) o1).getValue());
            }
        });
        for (Object e : a) {
            System.out.println(((Map.Entry<String, Double>) e).getKey() + " : "
                    + ((Map.Entry<String, Double>) e).getValue());
            resultPageIds.add(((Map.Entry<String, Double>) e).getKey());
            resultPagesScores.add(((Map.Entry<String, Double>) e).getValue());
        }

    }

    public static void main(String[] args) throws IOException {
//        ADPrediction adPrediction = new ADPrediction(7);
//        adPrediction.CalculateAD(1);
//        ArrayList<String> pageIDs = adPrediction.getResultPageIds();
//        ArrayList<Double> pageScores = adPrediction.getResultPagesScores();
//        for (String s:pageIDs){
//            System.out.println(s);
//        }
    }
}