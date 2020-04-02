package controller;

import javax.ws.rs.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Path("/sda")
public class SDAController {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public SDAController() throws IOException {
    }

    @GET
    public String whoAmI() {
        return "COMP4601 Searchable Document Archive: Junhao Chen And Mr. Liu";
    }
}
