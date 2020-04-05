package edu.carleton.comp4601.resources;

import edu.carleton.comp4601.dao.Community.CommunityCalculator;
import edu.carleton.comp4601.dao.impl.PageDAO;
import edu.carleton.comp4601.dao.impl.UserDAO;
import edu.carleton.comp4601.dao.modal.User;
import edu.carleton.comp4601.service.ADPrediction;
import edu.carleton.comp4601.service.Config;
import edu.carleton.comp4601.service.UserPrediction;

import javax.ws.rs.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;

@Path("/rs")
public class ContextualAdvertisingSystem {
    UserDAO userd = UserDAO.getInstance();

    PageDAO pageDAO = PageDAO.getInstance();

    UserPrediction userPrediction = UserPrediction.getInstance();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private String errorInfo = "";

    private String error = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "\t<meta charset=\"utf-8\">\n" +
            "\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "\t<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n" +
            "\n" +
            "\t<title>404 HTML Template by Colorlib</title>\n" +
            "\n" +
            "\t<!-- Google font -->\n" +
            "\t<link href=\"https://fonts.googleapis.com/css?family=Montserrat:200,400,700\" rel=\"stylesheet\">\n" +
            "\n" +
            "\t<!-- Custom stlylesheet -->\n" +
            "\n" +
            "\t<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\n" +
            "\t<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->\n" +
            "\t<!--[if lt IE 9]>\n" +
            "\t\t  <script src=\"https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js\"></script>\n" +
            "\t\t  <script src=\"https://oss.maxcdn.com/respond/1.4.2/respond.min.js\"></script>\n" +
            "\t\t<![endif]-->\n" +
            "\t<style type=\"text/css\">\n" +
            "\t\t* {\n" +
            "  -webkit-box-sizing: border-box;\n" +
            "          box-sizing: border-box;\n" +
            "}\n" +
            "\n" +
            "body {\n" +
            "  padding: 0;\n" +
            "  margin: 0;\n" +
            "}\n" +
            "\n" +
            "#notfound {\n" +
            "  position: relative;\n" +
            "  height: 100vh;\n" +
            "}\n" +
            "\n" +
            "#notfound .notfound {\n" +
            "  position: absolute;\n" +
            "  left: 50%;\n" +
            "  top: 50%;\n" +
            "  -webkit-transform: translate(-50%, -50%);\n" +
            "      -ms-transform: translate(-50%, -50%);\n" +
            "          transform: translate(-50%, -50%);\n" +
            "}\n" +
            "\n" +
            ".notfound {\n" +
            "  max-width: 520px;\n" +
            "  width: 100%;\n" +
            "  line-height: 1.4;\n" +
            "  text-align: center;\n" +
            "}\n" +
            "\n" +
            ".notfound .notfound-404 {\n" +
            "  position: relative;\n" +
            "  height: 200px;\n" +
            "  margin: 0px auto 20px;\n" +
            "  z-index: -1;\n" +
            "}\n" +
            "\n" +
            ".notfound .notfound-404 h1 {\n" +
            "  font-family: 'Montserrat', sans-serif;\n" +
            "  font-size: 236px;\n" +
            "  font-weight: 200;\n" +
            "  margin: 0px;\n" +
            "  color: #211b19;\n" +
            "  text-transform: uppercase;\n" +
            "  position: absolute;\n" +
            "  left: 50%;\n" +
            "  top: 50%;\n" +
            "  -webkit-transform: translate(-50%, -50%);\n" +
            "      -ms-transform: translate(-50%, -50%);\n" +
            "          transform: translate(-50%, -50%);\n" +
            "}\n" +
            "\n" +
            ".notfound .notfound-404 h2 {\n" +
            "  font-family: 'Montserrat', sans-serif;\n" +
            "  font-size: 28px;\n" +
            "  font-weight: 400;\n" +
            "  text-transform: uppercase;\n" +
            "  color: #211b19;\n" +
            "  background: #fff;\n" +
            "  padding: 10px 5px;\n" +
            "  margin: auto;\n" +
            "  display: inline-block;\n" +
            "  position: absolute;\n" +
            "  bottom: 0px;\n" +
            "  left: 0;\n" +
            "  right: 0;\n" +
            "}\n" +
            "\n" +
            ".notfound a {\n" +
            "  font-family: 'Montserrat', sans-serif;\n" +
            "  display: inline-block;\n" +
            "  font-weight: 700;\n" +
            "  text-decoration: none;\n" +
            "  color: #fff;\n" +
            "  text-transform: uppercase;\n" +
            "  padding: 13px 23px;\n" +
            "  background: #ff6300;\n" +
            "  font-size: 18px;\n" +
            "  -webkit-transition: 0.2s all;\n" +
            "  transition: 0.2s all;\n" +
            "}\n" +
            "\n" +
            ".notfound a:hover {\n" +
            "  color: #ff6300;\n" +
            "  background: #211b19;\n" +
            "}\n" +
            "\n" +
            "@media only screen and (max-width: 767px) {\n" +
            "  .notfound .notfound-404 h1 {\n" +
            "    font-size: 148px;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "@media only screen and (max-width: 480px) {\n" +
            "  .notfound .notfound-404 {\n" +
            "    height: 148px;\n" +
            "    margin: 0px auto 10px;\n" +
            "  }\n" +
            "  .notfound .notfound-404 h1 {\n" +
            "    font-size: 86px;\n" +
            "  }\n" +
            "  .notfound .notfound-404 h2 {\n" +
            "    font-size: 16px;\n" +
            "  }\n" +
            "  .notfound a {\n" +
            "    padding: 7px 15px;\n" +
            "    font-size: 14px;\n" +
            "  }\n" +
            "}\n" +
            "\t</style>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "\t<div id=\"notfound\">\n" +
            "\t\t<div class=\"notfound\">\n" +
            "\t\t\t<div class=\"notfound-404\">\n" +
            "\t\t\t\t<h1>Oops!</h1>\n" +
            "\t\t\t\t<h2>";

    public ContextualAdvertisingSystem() throws IOException {
    }

    @GET
    public String whoAmI() {
        return "COMP4601 Searchable Document Archive: Junhao Chen And Mr. Liu";
    }

    @GET
    @Path("context")
    @Produces(MediaType.TEXT_HTML)
    public String getContext(){
        List<User> users = userd.findAllUsers();

        String table = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n"+
                "<h2>Context</h2>\n";

        table += "<table><tr>" +
                "<th>User</th>" +
                "<th>Avg Score</th>" +
                "<th>No. reviews</th>" +
                "<th>positive review from other user</th>" +
                "<th>negative review from other user</th>" +
                "<th>helpful review (%)</th>" +
                "</tr>";

        for (User u : users){
            table+= "<tr>" +
                    "<th>"+u.getId()+"</th>" +
                    "<th>"+u.getScoreAvg()+"</th>" +
                    "<th>"+u.getReviews().size()+"</th>" +
                    "<th>"+ (int) (u.getThumbsFromOthers() * u.getHelpful()) +"</th>" +
                    "<th>"+(int)(u.getThumbsFromOthers()-(u.getThumbsFromOthers()*u.getHelpful()))+"</th>" +
                    "<th>"+u.getHelpful()+"</th>" +
                    "</tr>";
        }
        table += "</table></body></html>";
        Config.context = true;
        return table;
    }


    @GET
    @Path("community")
    @Produces(MediaType.TEXT_HTML)
    public String getCommunity() throws IOException {
        errorInfo = "Please first run the \"context\""+"</h2>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "\n" +
                "</body><!-- This templates was made by Colorlib (https://colorlib.com) -->\n" +
                "\n" +
                "</html>\n";
        String table = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h2>Community(Clusters)</h2>\n";

        table += "<table><tr>";
        for (int i=0;i<Config.optimalK;i++){
            table+="<th>"+(i+1)+"</th>";
        }
        table+="</tr>";

        if (Config.context){
            List<User> users = userd.findAllUsers();
            //System.out.println(Config.optimalK+"optimal k ---------------------------------------------------------");

            CommunityCalculator cc = new CommunityCalculator(users.size(),Config.optimalK);
            for (User user : users) {
                cc.addUser(user);
            }
            cc.algorithm();
            int max = cc.getBiggestClusterSize();

            for (int i=0;i<max;i++){
                for (int j=0;j<Config.optimalK;j++){
                    ArrayList<String> temp = cc.getAllUsersNameInCluster(j);
                    table+=addTable(i,temp);
                }
                table += "</tr>";
            }
            table += "</table></body></html>";
            return table;
        }
        return error+=errorInfo;
    }

    private String addTable(int current,ArrayList<String> str){
        String temp = "";
        if (current<str.size()){
            temp += "<th>" + str.get(current) + "</th>";
        }else {
            temp += "<th> </th>";
        }
        return temp;
    }

    @GET
    @Path("advertising/{category}")
    @Produces(MediaType.TEXT_HTML)
    public String getAd(@PathParam("category") String category){
        ADPrediction adPrediction = new ADPrediction(Config.optimalK);
        String table = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "display:block;\n" +
                "    text-decoration:none;"+
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h2>Advertising</h2>\n";

        table += "<table><tr>" +
                "<th>Page Name</th>" +
                "<th>Page Score</th>" +
                "<th>Page Link</th>" +
                "</tr>";

        String pageDefaultURL = "https://sikaman.dyndns.org:8443/WebSite/rest/site/courses/4601/assignments/training/pages/";
        try {
            int categoryInt = Integer.parseInt(category)-1;
            if (1<=(categoryInt+1)&&(categoryInt+1)<=Config.optimalK){
                adPrediction.CalculateAD(categoryInt);
                ArrayList<String> pageIDs = adPrediction.getResultPageIds();
                ArrayList<Double> pageScores = adPrediction.getResultPagesScores();
//                System.out.println(Config.optimalK+"optimal k ---------------------------------------------------------");
//                System.out.println(pageIDs.size()+"pageIds size ---------------------------------------------------------");
//                System.out.println(pageScores.size()+"pagescore size ---------------------------------------------------------");

                if (pageIDs.size()<15){
                    for (int i=0;i<pageIDs.size();i++){
                        table += "<table><tr>" +
                                "<th>"+pageIDs.get(i)+"</th>" +
                                "<th>"+pageScores.get(i)+"</th>" +
                                "<th>"+("<a href=\""+pageDefaultURL+pageIDs.get(i)+".html"+"\">"+pageIDs.get(i)+"</a>")+"</th>" +
                                "</tr>";
                    }
                }else {
                    for (int i=0;i<15;i++){
                        table += "<table><tr>" +
                                "<th>"+pageIDs.get(i)+"</th>" +
                                "<th>"+pageScores.get(i)+"</th>" +
                                "<th>"+("<a href=\""+pageDefaultURL+pageIDs.get(i)+".html"+"\">"+pageIDs.get(i)+"</a>")+"</th>" +
                                "</tr>";
                    }
                }

                table += "</table></body></html>";
                return table;
            }else{
                errorInfo = "Please input an Integer From 1-"+(Config.optimalK)+" for \"category\""+"</h2>\n" +
                        "\t\t\t</div>\n" +
                        "\t\t</div>\n" +
                        "\t</div>\n" +
                        "\n" +
                        "</body><!-- This templates was made by Colorlib (https://colorlib.com) -->\n" +
                        "\n" +
                        "</html>\n";

                return error+=errorInfo;
            }

        }catch (Exception e){
            errorInfo = "Please input an Integer for \"category\""+"</h2>\n" +
                    "\t\t\t</div>\n" +
                    "\t\t</div>\n" +
                    "\t</div>\n" +
                    "\n" +
                    "</body><!-- This templates was made by Colorlib (https://colorlib.com) -->\n" +
                    "\n" +
                    "</html>\n";
            return error+=errorInfo;
        }
    }

    @GET
    @Path("/fetch/{user}/{page}")
    @Produces(MediaType.TEXT_HTML)
    public String getAd(@PathParam("user") String userId,@PathParam("page")String pageId){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><head></head><body>");
        stringBuilder.append("</body></html>");
        stringBuilder.append(pageDAO.getById(pageId).getContent());
        stringBuilder.append("--------");
        Map<String,Double> result = userPrediction.predictionUser(userd.getById(userId));
        String maxId =  result.entrySet().stream().filter(e->!e.getKey().equals(pageId)).max(Comparator.comparingDouble(Map.Entry::getValue)).get().getKey();
        stringBuilder.append(pageDAO.getById(maxId).getContent());
        stringBuilder.append(maxId);
        stringBuilder.append("</body></html>");
        return stringBuilder.toString();
    }


}
