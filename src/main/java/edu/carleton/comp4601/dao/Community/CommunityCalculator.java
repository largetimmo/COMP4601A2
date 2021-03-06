package edu.carleton.comp4601.dao.Community;
import edu.carleton.comp4601.dao.modal.User;

import java.io.*;
import java.util.*;

public class CommunityCalculator {
    private int no_users;
    private CUser[] users;
    private int no_features=10;
    private boolean changed;
    private int no_clusters = 0;
    private int user_count = 0;
    private double ss = 0.0;
    private ArrayList<User> allUsers;

    /*
     * Constructor that reads the data in from a file.
     * You must specify the number of clusters.
     */
    public CommunityCalculator(int noUsers,int no_clusters){
        changed = true;
        this.no_clusters = no_clusters;
        this.no_users = noUsers;
        this.users = new CUser[noUsers];
        allUsers = new ArrayList<>();
    }

    /*
     * This is where your implementation goes
     */
    public void algorithm() {
        List<CUser> c = new ArrayList<>();
        //decide position k and add it to c
        for (int i=0;i<no_clusters;i++){
            CUser temp = new CUser(users[i].name,users[i].features.length,users[i].distance.length);
            temp.cluster = users[i].cluster;
            temp.last_cluster = users[i].last_cluster;
            temp.features = Arrays.copyOf(users[i].features,users[i].features.length);
            temp.distance = Arrays.copyOf(users[i].distance,users[i].distance.length);
            c.add(temp);
        }

        while (changed) {
            // Your code here
            changed = false;
            //find the distance between all users and the k points
            for (int i=0;i<no_users;i++){
                for (int j=0;j<c.size();j++){
                    users[i].distance[j] = distance(users[i],c.get(j));
                }
            }

            //user find the minimum distance cluster and update
            for (int i=0;i<no_users;i++){
                List<Double> distance = new ArrayList<>();
                for (double d:users[i].distance) distance.add(d);
                int minIndex = distance.indexOf(Collections.min(distance));
                users[i].update();
                users[i].cluster = minIndex;
            }

            //
            for (int i=0;i<c.size();i++){
                List<Double> ave = new ArrayList<>();
                for (int j=0;j<no_features;j++) ave.add(0d);
                int count=0;
                for (int j=0;j<no_users;j++){
                    if (users[j].cluster == i){
                        for (int a=0;a< ave.size();a++){
                            ave.set(a, ave.get(a)+users[j].features[a]);
                        }
                        count ++;
                    }
                }
                for (int j=0;j<ave.size();j++){
                    ave.set(j,ave.get(j)/count);
                }
                for (int j=0;j<no_features;j++){
                    c.get(i).features[j] = ave.get(j);
                }
            }
            changed = Arrays.stream(users).anyMatch(CUser::changed);
        }
        int tempCount = 0;
        for(int i = 0; i < c.size();i++){
            ArrayList<Double> distances = new ArrayList<>();
            double totalDistance = 0d;
            int count = 0;
            for(int j =0 ; j< no_users ; j++){
                if(users[j].cluster == i){
                    totalDistance += distance(users[j],c.get(i));
                    distances.add(distance(users[j],c.get(i)));
                    count++;
                }
            }
            Double mean = totalDistance/count;

            for (int h=0;h<distances.size();h++){
                ss+=Math.pow((distances.get(h)-mean),2);
                tempCount++;
            }
            //System.out.println("Cluster: "+i+" Total distance:"+totalDistance+", Total: "+count + " SS is: "+ss);
        }
        ss /= tempCount;
    }

    public double getSs() {
        return ss;
    }

    private void addUserHelper(String name, ArrayList<Double> features){
        users[user_count] = new CUser(name,no_features,no_clusters);
        for (int i = 0;i<no_features;i++){
            users[user_count].features[i] = features.get(i);
        }
        user_count++;
    }

    public void addUser(User user){
        ArrayList<Double> d = generateUserFeatures(user);
        addUserHelper(user.getId(),d);
        allUsers.add(user);
    }

    public ArrayList<User> getAllUsersInCluster(User u){
        int cluster = getClusterByUserName(u.getId());
        ArrayList<String> cusers = new ArrayList<>();
        for (CUser u1: users){
            if (u1.cluster == cluster){
                cusers.add(u1.name);
            }
        }

        ArrayList<User> returnList = new ArrayList<>();
        for (User u1: allUsers){
            if (cusers.contains(u1.getId())){
                returnList.add(u1);
            }
        }
        return returnList;
    }

    public ArrayList<User> getAllUsersInClusterById(int i){
        int cluster = i;
        ArrayList<String> cusers = new ArrayList<>();
        for (CUser u1: users){
            if (u1.cluster == cluster){
                cusers.add(u1.name);
            }
        }

        ArrayList<User> returnList = new ArrayList<>();
        for (User u1: allUsers){
            if (cusers.contains(u1.getId())){
                returnList.add(u1);
            }
        }
        return returnList;
    }


    public ArrayList<String> getAllUsersNameInCluster(int i){
        ArrayList<String > temp = new ArrayList<>();
        for (CUser u: users){
            if (u.cluster == i){
                temp.add(u.name);
            }
        }
        return temp;
    }

    public int getBiggestClusterSize(){
        ArrayList<Integer> size = new ArrayList<>();
        for (int i=0;i<no_clusters;i++){
            size.add(getAllUsersNameInCluster(i).size());
        }
//        size.add(getAllUsersNameInCluster(0).size());
//        size.add(getAllUsersNameInCluster(1).size());
//        size.add(getAllUsersNameInCluster(2).size());
//        size.add(getAllUsersNameInCluster(3).size());
//        size.add(getAllUsersNameInCluster(4).size());
        return Collections.max(size);
    }

    private ArrayList<Double> generateUserFeatures(User user){
        //avg score (5) -> no. review (100暂定) -> positive from other (8000 暂定)-> negative from other (8000暂定)
        //-> helpful (1) -> very positive sentiment(1433) -> positive sentiment (5045)
        //->neutral (3360) -> nagative (8233) -> very negative (337)
        ArrayList<Double> temp = new ArrayList<>();

        temp.add(user.getScoreAvg()*10/5);
        temp.add((double) ((user.getReviews().size()*10)/478));
        temp.add(user.getHelpful()*user.getThumbsFromOthers()*10/8000);
        temp.add((1-user.getHelpful())*user.getThumbsFromOthers()*10/8000);
        temp.add(user.getHelpful()*10);
        temp.add((double)user.getVeryPositive()*10/1433);
        temp.add((double)user.getPositive()*10/5045);
        temp.add((double)user.getNatural()*10/8233);
        temp.add((double)user.getNegative()*10/8233);
        temp.add((double)user.getVeryNegative()*10/337);
        return temp;
    }

    public int getClusterByUserName(String name){
        for (CUser u: users){
            if (u.name.equals(name)){
                return u.cluster;
            }
        }
        return -1;
    }

    /*
     * Computes distance between two users
     * Could implement this on User too.
     */
    private double distance(CUser a, CUser b) {
        double rtn = 0.0;
        // Assumes a and b have same number of features
        for (int i = 0; i < a.features.length; i++) {
            rtn += (a.features[i] - b.features[i])
                    * (a.features[i] - b.features[i]);
        }
        return Math.sqrt(rtn);
    }

    public static void main(String[] args) throws IOException {

//        UserDAO userd = UserDAO.getInstance();
//        List<User> users = userd.findAllUsers();
//        System.out.println("optimalk: "+k);
    }

    // Private class for representing user
    private class CUser {
        public double[] features;
        public double[] distance;
        public String name;
        public int cluster;
        public int last_cluster;

        public CUser(String name, int noFeatures, int noClusters) {
            this.name = name;
            this.features = new double[noFeatures];
            this.distance = new double[noClusters];
            this.cluster = -1;
            this.last_cluster = -2;
        }

        // Check if cluster association has changed.
        public boolean changed() {
            return last_cluster != cluster;
        }

        // Update the saved cluster from iteration to iteration
        public void update() {
            last_cluster = cluster;
        }

        public String toString() {
            StringBuffer b = new StringBuffer(name);
            for (int i = 0; i < features.length; i++) {
                b.append(' ');
                b.append(features[i]);
            }
            return b.toString();
        }
    }
}
