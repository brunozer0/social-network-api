package br.com.application.quarkussocial.rest;


import br.com.application.quarkussocial.domain.model.Post;
import br.com.application.quarkussocial.domain.model.User;
import br.com.application.quarkussocial.domain.repository.PostRepository;
import br.com.application.quarkussocial.domain.repository.UserRepository;
import br.com.application.quarkussocial.rest.dto.CreatePostRequest;
import br.com.application.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.annotations.Pos;

import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class PostResource {

    private UserRepository userRepository;
    private PostRepository repository;

    @Inject
    public PostResource (
            UserRepository userRepository, PostRepository repository){
    this.userRepository = userRepository;

        this.repository = repository;
    }

    @POST
    @Transactional
    public Response savePosts(@PathParam("userId") long userId, CreatePostRequest request){
        User user = userRepository.findById(userId);
        if(user == null ){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        repository.persist(post);
        return  Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") long userId){

        User user = userRepository.findById(userId);
        if(user == null ){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PanacheQuery<Post> query = repository.find(
                "user", Sort.by("dateTime", Sort.Direction.Descending), user);

        //  inferencia  de tipos, parecido com typescript
        var list   = query.list();

        var postResponseList =list.stream()

                //.map(post -> PostResponse.fromEntity(post))

                .map(PostResponse::fromEntity) //method referencia
                .collect(Collectors.toList());
        return  Response.ok(postResponseList).build();
    }
}
