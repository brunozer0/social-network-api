package br.com.application.quarkussocial.rest;

import br.com.application.quarkussocial.domain.model.User;
import br.com.application.quarkussocial.domain.repository.UserRepository;
import br.com.application.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    Long userId;
    @BeforeEach
    @Transactional
    public void setUP(){

        var user = new User();
        user.setAge(39);
        user.setName("juao");

        userRepository.persist(user);
        userId = user.getId();
    }
    @Test
    @DisplayName("Should creatte a post for a user")
    public  void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text");

        var userID= 1;

            given()
                    .contentType(ContentType.JSON)
                    .body(postRequest)
                    .pathParam("userId", userId)
            .when()
                  .post()

            .then()
                .statusCode(201);
    }
}