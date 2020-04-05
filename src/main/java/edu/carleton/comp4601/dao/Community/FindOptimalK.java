package edu.carleton.comp4601.dao.Community;

import edu.carleton.comp4601.dao.impl.UserDAO;
import edu.carleton.comp4601.dao.modal.User;

import java.util.ArrayList;
import java.util.List;

public class FindOptimalK {
    private static final FindOptimalK INSTANCE = new FindOptimalK();
    public static FindOptimalK getInstance(){
        return INSTANCE;
    }
    private UserDAO userDAO = UserDAO.getInstance();
    private CommunityCalculator communityCalculator;
    private int optimalk = 0;

    public FindOptimalK(){
        List<User> users = userDAO.findAllUsers();
        optimalk = findOptimalK();
    }

    public int getOptimalk() {
        return optimalk;
    }

    private int findOptimalK() {
        List<User> users = userDAO.findAllUsers();
        ArrayList<Double> findK = new ArrayList<>();
        int optimalK = 0;
        for (int i=0;i<100;i++){
            CommunityCalculator cc = new CommunityCalculator(users.size(),i+1);
            for (User user : users) {
                cc.addUser(user);
            }
            cc.algorithm();
            findK.add(cc.getSs());
        }
        for (int i=0;i< findK.size();i++){
            if (i+1<findK.size()){
                if (findK.get(i)-findK.get(i+1) <= 0.01 && optimalK==0){
                    optimalK = i;
                }
            }
        }
        return optimalK;
    }

}
