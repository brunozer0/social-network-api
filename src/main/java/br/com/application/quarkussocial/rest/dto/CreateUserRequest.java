package br.com.application.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name Is Required")
    private String name;
    @NotNull(message = "Age Is Required")
    private Integer age;


}
