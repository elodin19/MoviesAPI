package com.cleverpy.moviesAPI.entities;

import com.cleverpy.moviesAPI.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity that manages the users in the database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "activation_code")
    private Integer activationCode;

    @Column(name = "is_activated")
    private Boolean isActivated = false;

    @Column(name = "validation_code")
    private Integer validationCode;

    @Column(name = "time_stamp")
    private Timestamp timeStamp;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID") })
    private List<Role> roles = new ArrayList<>();

    public User(String email, String username, String password, List<Role> roles){
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserResponseDto getDto(String message){

        List<String> strRoles = new ArrayList<>();
        for (Role role : roles) strRoles.add(role.getName());

        return new UserResponseDto(username, email, isActivated, strRoles, message);
    }
}
