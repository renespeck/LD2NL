package org.aksw.owl2nl.restapi;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import java.awt.*;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.aksw.owl2nl.OWLAxiomConverter;

import org.checkerframework.common.reflection.qual.GetClass;


@Path("resource1")
public class Resource1 {



   /* @GET public String testing() {
        return "HELLO WORLD";
    }*/
    private static final OWLAxiomConverter Aconvertor=new OWLAxiomConverter();
    @Path("/geturl")
    @GET
    @Produces(MediaType.TEXT_PLAIN)

    public String geturl(@QueryParam("URL")String url) throws Exception {
        String summary= null;

        summary = Aconvertor.Checker(url);

        return summary;
    }




}

