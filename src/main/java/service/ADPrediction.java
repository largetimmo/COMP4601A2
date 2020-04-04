package service;


import dao.Community.CommunityCalculator;
import dao.impl.PageDAO;
import dao.impl.UserDAO;
import dao.modal.User;

import java.util.List;

public class ADPrediction {
    private static final ADPrediction INSTANCE = new ADPrediction();
    public static ADPrediction getInstance(){
        return INSTANCE;
    }
    private UserDAO userDAO = UserDAO.getInstance();
    private PageDAO pageDAO = PageDAO.getInstance();
    private CommunityCalculator communityCalculator;

    public ADPrediction(){
        List<User> users = userDAO.findAllUsers();
        int optimalk = Config.optimalK;
        communityCalculator = new CommunityCalculator(users.size(),optimalk);
        users.forEach(communityCalculator::addUser);
        communityCalculator.algorithm();
    }
}