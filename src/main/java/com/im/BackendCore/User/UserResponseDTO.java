package com.im.BackendCore.User;

import com.im.BackendCore.Enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private Date birthdate;
    private String email;
    private String urlPhoto;
    private UserRole role;
    private Date createdAt;
    private Date updatedAt;

    // Static factory method to create from UserModel
    public static UserResponseDTO fromUserModel(UserModel user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setBirthdate(user.getBirthdate());
        dto.setEmail(user.getEmail());
        dto.setUrlPhoto(user.getUrlPhoto());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}