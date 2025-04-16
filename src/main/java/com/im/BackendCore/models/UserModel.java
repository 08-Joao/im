package com.im.BackendCore.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="tb_usuarios")
@Getter
@Setter
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date birthdate;
    private String email;
    private String password;
    private String urlPhoto;

    public UserModel() {
    }

    public UserModel(String name, Date birthdate, String email, String password, String urlPhoto) {
        this.name = name;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
        this.urlPhoto = urlPhoto;
    }

}
