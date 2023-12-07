package br.com.application.quarkussocial.rest;

import br.com.application.quarkussocial.domain.model.Follower;
import br.com.application.quarkussocial.domain.model.User;
import br.com.application.quarkussocial.domain.repository.FollowerRepository;
import br.com.application.quarkussocial.domain.repository.UserRepository;
import br.com.application.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    Long userId;
    Long followerId;
    @BeforeEach
    @Transactional
    void setUp(){

        //default user tests
        var user = new User();
        user.setAge(39);
        user.setName("juao");
        userRepository.persist(user);
        userId = user.getId();


        // follower
        var follower = new User();
        follower.setAge(29);
        follower.setName("julia");
        userRepository.persist(follower);
        followerId = follower.getId();

        // Create Follower
        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);

        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("Should return 409 when followerId is equal to userId ")
    public void sameUserAsFollowerTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("Invalid Action: Can't follow YourSelf"));
    }

    @Test
    @DisplayName("Should return 404 when User id doesn't exist")
    public void userNotFoundWhenTryingToFollowTest(){


        var body = new FollowerRequest();
        body.setFollowerId(userId);
        var inexistentUserID =999;
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserID)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("Should follow a user ")
    public void followUserTest(){


        var body = new FollowerRequest();
        body.setFollowerId(followerId);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }

    @Test
    @DisplayName("Should return 404 on list User followers")
    public void userNotFoundWhenListingFollowersTest(){

        var inexistentUserID =999;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserID)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("should list a user's followers")
    public void listFollowersTest(){

    var response =
                given()
                        .contentType(ContentType.JSON)
                        .pathParam("userId", userId)
                .when()
                        .get()
                .then()
                        .extract().response();

        var followersCount =response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());
    }

    @Test
    @DisplayName("Should return 404 on unfollow user and user id doen't exist")
    public void userNotFoundWhenUnfollowingAUserTest(){

        var inexistentUserID =999;

        given()
                .pathParam("userId", inexistentUserID)
                .queryParam("followerId", followerId)

        .when()
            .delete()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @DisplayName("Should Unfollow an user")
    public void unfollowUserTest(){

        given()
                .pathParam("userId", userId)
                .queryParam("followerId", followerId)

        .when()
            .delete()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    }
}