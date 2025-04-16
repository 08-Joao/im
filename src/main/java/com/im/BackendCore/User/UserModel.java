package com.im.BackendCore.User;

import com.im.BackendCore.Property.PropertyModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "owner")
    private List<PropertyModel> ownedProperties;

    @OneToMany(mappedBy = "lessor")
    private List<PropertyModel> rentedProperties;

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
