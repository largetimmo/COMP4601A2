package dao.Community;
import java.io.*;
import java.util.*;

public class CommunityCalculator {
    private int no_users;
    private CUser[] users;
    private int no_features=5;
    private boolean changed;
    private int no_clusters = 5;

    /*
     * Constructor that reads the data in from a file.
     * You must specify the number of clusters.
     */
    public CommunityCalculator(int noUsers){


        changed = true;

        this.no_users = noUsers;
        this.users = new CUser[noUsers];
    }

    /*
     * This is where your implementation goes
     */
    private void algorithm() throws IOException {
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


        for (int i = 0; i < no_users; i++) {

            System.out.println(users[i].toString());
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
        for(int i = 0; i < c.size();i++){
            double totalDistance = 0d;
            int count = 0;
            for(int j =0 ; j< no_users ; j++){
                if(users[j].cluster == i){
                    totalDistance += distance(users[j],c.get(i));
                    count++;
                }
            }
            System.out.println("Cluster: "+i+" Total distance:"+totalDistance+", Total: "+count);
        }
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

    public static void main(String[] args) {
//        try {
//            int numberOfClusters = 2;
//            String fileName = System.getProperty("user.dir")+"\\KNN-1.txt";
//            Kmeans knn = new Kmeans(numberOfClusters, new File(fileName));
//            knn.algorithm();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
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
