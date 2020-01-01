package RestApi;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class config extends Application{
    private Set<Class<?>> resources= new HashSet<>();

    public config(){
        System.out.println("Created config");
        resources.add(Resource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return resources;
    }
}
