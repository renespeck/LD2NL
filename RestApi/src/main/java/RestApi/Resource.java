package org.aksw.RestApi;
import org.aksw.owl2nl.OWLAxiomConverter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/restowl")
public class Resource {

    private static final OWLAxiomConverter Aconvertor=new OWLAxiomConverter();
    @Path("/geturl")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String geturl(@QueryParam("URL")String url) throws Exception {
        String summary= null;

        summary = Aconvertor.Checker(url);

        return summary;
    }
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "test";
    }
}

