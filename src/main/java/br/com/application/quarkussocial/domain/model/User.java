package br.com.application.quarkussocial.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "Users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name ="name")
    private  String name;
    @Column (name ="age")
    private Integer age;


}