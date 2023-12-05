package br.com.application.quarkussocial.rest;


import br.com.application.quarkussocial.domain.model.Follower;
import br.com.application.quarkussocial.domain.repository.FollowerRepository;
import br.com.application.quarkussocial.domain.repository.UserRepository;
import br.com.application.quarkussocial.rest.dto.FollowerRequest;
import br.com.application.quarkussocial.rest.dto.FollowerResponse;
import br.com.application.quarkussocial.rest.dto.FollowersPerUserResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.ResponseObject;

import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class FollowerResource{

    private FollowerRepository repository;
    private UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository repository, UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(
            @PathParam("userId") Long userId, FollowerRequest request){

        if(userId.equals(request.getFollowerId())){

            return  Response.status(Response.Status.CONFLICT)
                    .entity("Invalid Action: Can't follow YourSelf")
                    .build();
        }
       var user = userRepository.findById(userId);

       if(user == null) {
           return Response.status(Response.Status.NOT_FOUND).build();
       }

       var follower = userRepository.findById(request.getFollowerId());

        boolean follows = repository.follows(follower, user);

        if(!follows){
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            repository.persist(entity);

        }

       return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){

        var user = userRepository.findById(userId);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        var list = repository.findByUser(userId);

        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();

        responseObject.setFollowersCount(list.size());


        var followerList= list.stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());

        responseObject.setContent(followerList);

        return Response.ok(responseObject).build();

    }
}
