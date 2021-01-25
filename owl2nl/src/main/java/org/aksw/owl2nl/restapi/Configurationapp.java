package org.aksw.owl2nl.restapi;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("owl")
public class Configurationapp extends Application{
    private Set<Class<?>> resources= new HashSet<>();

    public Configurationapp(){
        System.out.println("Created Configurationapp");
        resources.add(Resource1.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return resources;

    }


}
