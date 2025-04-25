package com.im.BackendCore.User;

import com.im.BackendCore.Enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String name;
    private Date birthdate;
    private String email;
    private String password;
    private String urlPhoto;
}

