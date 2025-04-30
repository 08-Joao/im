package com.im.BackendCore.User;

import com.im.BackendCore.Enums.UserRole;
import com.im.BackendCore.Property.PropertyModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="tb_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date birthdate;
    private String cpf;
    private String email;
    private String password;
    private String urlPhoto;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "owner")
    private List<PropertyModel> ownedProperties;

    @OneToMany(mappedBy = "lessor")
    private List<PropertyModel> rentedProperties;


}
