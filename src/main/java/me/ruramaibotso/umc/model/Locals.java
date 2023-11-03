package me.ruramaibotso.umc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ruramaibotso.umc.user.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Locals {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String location;
    private String prefix;
    @OneToMany
    @JsonIgnore
    private List<Section> sections;
    @OneToMany
    @JsonIgnore
    private List<User> users;
    @OneToMany
    @JsonIgnore
    private List<Organisations> organisations;
}
