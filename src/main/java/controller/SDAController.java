package controller;

import javax.ws.rs.*;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
                "<th>Ave Score</th>" +
                "<th>No. reviews</th>" +
                "<th>positive review from other user</th>" +
                "<th>negative review from other user(%)</th>" +
                "<th>helpful review (%)</th>" +
                "</tr>";
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
