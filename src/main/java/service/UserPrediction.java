package service;

import dao.Community.CommunityCalculator;
import dao.impl.PageDAO;
import dao.impl.ReviewDAO;
import dao.impl.UserDAO;
import dao.modal.Page;
import dao.modal.Review;
import dao.modal.User;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserPrediction {

    private static final UserPrediction INSTANCE = new UserPrediction();
    public static UserPrediction getInstance(){
        return INSTANCE;
    }

    private static final Double NO_VALUE = Double.MIN_VALUE;

    private UserDAO userDAO = UserDAO.getInstance();

    private PageDAO pageDAO = PageDAO.getInstance();

    private ReviewDAO reviewDAO = ReviewDAO.getInstance();

    private CommunityCalculator communityCalculator;

    public UserPrediction() {
        List<User> users = userDAO.findAllUsers();
        communityCalculator = new CommunityCalculator(users.size());
        users.forEach(communityCalculator::addUser);
        try {
            communityCalculator.algorithm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void predictUser(String userId, String pageId){
        List<User> users = userDAO.findAllUsers();
        List<Page> pages = pageDAO.findAllPages();

    }
    public Double userSimilarity(User user1, User user2){
        if(user1.equals(user2)){
            return 1d;
        }
        List<String> commonSite = new ArrayList<>();

        for(String id : user1.getViewedSite()){
            if(user2.getViewedSite().contains(id)){
                commonSite.add(id);
            }
        }
        if(commonSite.size() == 0){
            return NO_VALUE;
        }
        List<Review> firstUserReview = reviewDAO.findByUserId(user1.getId());
        List<Review> secondUserReview = reviewDAO.findByUserId(user2.getId());
        double firstUserAvg =  firstUserReview.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).sum()/commonSite.size();
        double secondUserAvg =  secondUserReview.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).sum()/commonSite.size();
        List<Double> firstUserDiff = new ArrayList<>();
        List<Double> secondUserDiff = new ArrayList<>();
        firstUserReview.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).forEach(s->{
            firstUserDiff.add(s-firstUserAvg);
        });
        secondUserReview.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).forEach(s->{
            secondUserDiff.add(s-secondUserAvg);
        });

        double upper = IntStream.range(0, firstUserDiff.size()).mapToDouble(i -> firstUserDiff.get(i) * secondUserDiff.get(i)).sum();
        double lower = Math.sqrt(firstUserDiff.stream().mapToDouble(n->Math.pow(n,2)).sum()) * Math.sqrt(secondUserDiff.stream().mapToDouble(n->Math.pow(n,2)).sum());
        return upper/lower;
    }

    public Map<String,Double> predictionUser(User user1){
        Map<String,Double> result = new HashMap<>();//item-->result
        double userAvg = user1.getScoreAvg();

        pageDAO.findAllPages().stream().filter(p->!user1.getViewedSite().contains(p.getId())).forEach(p->{
            //Find neighbours
            List<User> neighbours = communityCalculator.getAllUsersInCluster(user1)
                    .stream()
                    .filter(u->!u.getId().equals(user1.getId())) //not self
                    .filter(u->u.getViewedSite().contains(p.getId()))  //has viewed the page p
                    .filter(u->userSimilarity(user1,u) > 0)//sim > 0
                    .collect(Collectors.toList());

            double upper = 0;
            double lower = 0;
            for(User n : neighbours){
                double sim = userSimilarity(user1,n);
                double secondUserAvg = n.getScoreAvg();
                upper+= sim* reviewDAO.findByUserId(n.getId()).stream().mapToDouble(Review::getScore).map(k->k-secondUserAvg).sum();
                lower+=secondUserAvg;
            }

            result.put(p.getId(),userAvg+upper/lower);
        });
        return result;
    }
}
