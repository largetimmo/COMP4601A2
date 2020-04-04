package service;

import dao.Community.CommunityCalculator;
import dao.Community.FindOptimalK;
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

    private FindOptimalK findOptimalK = FindOptimalK.getInstance();

    private UserDAO userDAO = UserDAO.getInstance();

    private PageDAO pageDAO = PageDAO.getInstance();

    private ReviewDAO reviewDAO = ReviewDAO.getInstance();

    private CommunityCalculator communityCalculator;

    public UserPrediction() {
        List<User> users = userDAO.findAllUsers();
        Config.optimalK = findOptimalK.getOptimalk();
        int optimalk = Config.optimalK;
        communityCalculator = new CommunityCalculator(users.size(),optimalk);
        users.forEach(communityCalculator::addUser);
        communityCalculator.algorithm();
    }

    public void predictUser(String userId, String pageId){
        List<User> users = userDAO.findAllUsers();
        List<Page> pages = pageDAO.findAllPages();

    }
    public Double userSimilarity(User user1, User user2,List<Review> user1Review, List<Review> user2Review){
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
        double firstUserAvg =  user1Review.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).sum()/commonSite.size();
        double secondUserAvg =  user2Review.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).sum()/commonSite.size();
        List<Double> firstUserDiff = new ArrayList<>();
        List<Double> secondUserDiff = new ArrayList<>();
        user1Review.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).forEach(s->{
            firstUserDiff.add(s-firstUserAvg);
        });
        user2Review.stream().filter(r->commonSite.contains(r.getPageId())).mapToDouble(Review::getScore).forEach(s->{
            secondUserDiff.add(s-secondUserAvg);
        });

        double upper = IntStream.range(0, firstUserDiff.size()).mapToDouble(i -> firstUserDiff.get(i) * secondUserDiff.get(i)).sum();
        double lower = Math.sqrt(firstUserDiff.stream().mapToDouble(n->Math.pow(n,2)).sum()) * Math.sqrt(secondUserDiff.stream().mapToDouble(n->Math.pow(n,2)).sum());
        return upper/lower;
    }

    public Map<String,Double> predictionUser(User user1){
        Map<String,Double> result = new HashMap<>();//item-->result
        double userAvg = user1.getScoreAvg();
        List<User> communityMember = communityCalculator.getAllUsersInCluster(user1);
        Map<String,List<Review>> reviewCache = new HashMap<>();
        communityMember.parallelStream().forEach(u-> reviewCache.put(u.getId(),reviewDAO.findByUserId(u.getId())));
        pageDAO.findAllPages().stream().parallel().filter(p->!user1.getViewedSite().contains(p.getId())).forEach(p->{
            //Find neighbours
            List<User> neighbours = communityMember.stream()
                    .filter(u->!u.getId().equals(user1.getId())) //not self
                    .filter(u->u.getViewedSite().contains(p.getId()))  //has viewed the page p
                    .filter(u->userSimilarity(user1,u,reviewCache.get(user1.getId()),reviewCache.get(u.getId())) > 0)//sim > 0
                    .collect(Collectors.toList());

            double upper = 0;
            double lower = 0;
            for(User n : neighbours){
                double sim = userSimilarity(user1,n,reviewCache.get(user1.getId()),reviewCache.get(n.getId()));
                double secondUserAvg = n.getScoreAvg();
                upper+= sim* reviewCache.get(n.getId()).stream().mapToDouble(Review::getScore).map(k->k-secondUserAvg).sum();
                lower+=secondUserAvg;
            }
            System.out.println("finish");
            result.put(p.getId(),userAvg+upper/lower);
        });
        return result;
    }

}
