package com.boot.board_240214.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user.id"),
            inverseJoinColumns = @JoinColumn(name = "role.id"))
//    Set<Course> likedCourses;

    private List<Role> roles = new ArrayList<>();

}
