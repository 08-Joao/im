package com.im.BackendCore.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {
    private String name;
    private Date birthdate;
    private String email;
    private String password;
    private String newPassword;
    private String urlPhoto;
}
