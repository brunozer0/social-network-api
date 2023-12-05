package br.com.application.quarkussocial.rest;


import br.com.application.quarkussocial.domain.model.User;
import br.com.application.quarkussocial.domain.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class PostResource {

    private UserRepository userRepository;

    @Inject
    public PostResource (UserRepository userRepository){
    this.userRepository = userRepository;
    }

    @POST
    public Response savePosts(@PathParam("userId") long userId){
        User user = userRepository.findById(userId);
        if(user == null ){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return  Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") long userId){

        User user = userRepository.findById(userId);
        if(user == null ){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return  Response.ok().build();
    }
}
