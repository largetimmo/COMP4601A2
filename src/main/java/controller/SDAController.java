package controller;

import dao.CrawlDataDAO;
import dao.impl.CrawlDataDAOImpl;
import javax.ws.rs.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Path("/sda")
public class SDAController {
    public static CrawlDataDAO cdi;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public SDAController() throws IOException {
        cdi = CrawlDataDAOImpl.getInstance();
    }

    @GET
    public String whoAmI() {
        return "COMP4601 Searchable Document Archive: Junhao Chen And Mr. Liu";
    }
}
