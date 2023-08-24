package com.weather.api.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    private String fullName;

    @Column(unique = true)
    private String login;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Role role;
}
