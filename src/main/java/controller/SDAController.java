package controller;

import dao.impl.UserDAO;
import dao.modal.User;

import javax.ws.rs.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ws.rs.core.MediaType;

@Path("/sda")
public class SDAController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public SDAController() throws IOException {
    }

    @GET
    public String whoAmI() {
        return "COMP4601 Searchable Document Archive: Junhao Chen And Mr. Liu";
    }

    @GET
    @Path("context")
    @Produces(MediaType.TEXT_HTML)
    public String getContext(){
        UserDAO userd = new UserDAO();
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
        return table;
    }


    @GET
    @Path("community")
    @Produces(MediaType.TEXT_HTML)
    public String getCommunity(){
        return "";
    }

    @GET
    @Path("advertising/{category}")
    @Produces(MediaType.TEXT_HTML)
    public String getAd(@PathParam("category") String category){
        return "";
    }
}
