package br.com.application.quarkussocial.rest;

import br.com.application.quarkussocial.domain.model.Follower;
import br.com.application.quarkussocial.domain.model.Post;
import br.com.application.quarkussocial.domain.model.User;
import br.com.application.quarkussocial.domain.repository.FollowerRepository;
import br.com.application.quarkussocial.domain.repository.PostRepository;
import br.com.application.quarkussocial.domain.repository.UserRepository;
import br.com.application.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;
    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;
    @BeforeEach
    @Transactional
    public void setUP(){
        //Default user tests
        var user = new User();
        user.setAge(39);
        user.setName("juao");
        userRepository.persist(user);
        userId = user.getId();

        //post
        Post post = new Post();
        post.setText("hello");
        post.setUser(user);
        postRepository.persist(post);

        // not follower user
        var userNotFollower = new User();
        userNotFollower.setAge(20);
        userNotFollower.setName("bruno");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

         //follower user
        var userFollower = new User();
        userFollower.setAge(25);
        userFollower.setName("carlos");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }
    @Test
    @DisplayName("Should creatte a post for a user")
    public  void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

            given()
                    .contentType(ContentType.JSON)
                    .body(postRequest)
                    .pathParam("userId", userId)
            .when()
                  .post()

            .then()
                .statusCode(201);
    }


    @Test
    @DisplayName("Should return 404 when trying to a make post for an inexistent user")
    public  void postForAnInexistentUserTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var inexistentUserId= 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistentUserId)
        .when()
                .post()

        .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist ")
    public void listPostUserNotFoundTest() {
        var inexistentUserId= 999;

        given()
                .pathParam("userId",inexistentUserId)
        .when()
                .get()
        .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    public void listPostFollowerHeaderNotSendTest() {

        given()
                .pathParam("userId",userId)
        .when()
                .get()
        .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("Should return 400 when follower  doesn't exist")
    public void listPostFollowerNotFoundTest() {

        var inexistentFollowerId = 999;
        given()
                .pathParam("userId",userId)
                .header("followerId",inexistentFollowerId)
        .when()
                .get()
        .then()
                .statusCode(400)
                .body(Matchers.is("this followerId does not exist"));
    }

    @Test
    @DisplayName("Should return 403 when follower  isn't follower")
    public void listPostNotFollower() {

        given()
                .pathParam("userId",userId)
                .header("followerId",userNotFollowerId)
        .when()
                .get()
        .then()
                .statusCode(403)
                .body(Matchers.is("Follow to see the posts"));

    }


    @Test
    @DisplayName("Should return posts")
    public void listPostsTest() {

        given()
                .pathParam("userId",userId)
                .header("followerId",userFollowerId)
        .when()
                .get()
        .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));

    }
}