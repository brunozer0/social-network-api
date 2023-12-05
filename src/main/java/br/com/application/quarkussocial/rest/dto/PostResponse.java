package br.com.application.quarkussocial.rest.dto;

import br.com.application.quarkussocial.domain.model.Post;
import jakarta.ws.rs.core.Response;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String Text;
    private LocalDateTime dateTime;


    public static  PostResponse fromEntity(Post post){

        var response = new PostResponse();

        response.setText(post.getText());
        response.setDateTime(post.getDateTime());

        return response;
    }
}
