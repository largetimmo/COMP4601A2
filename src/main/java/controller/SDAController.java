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
        return "";
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
