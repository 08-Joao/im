package com.im.BackendCore.User;

import com.im.BackendCore.Enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public  Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel createUser(UserCreateDTO userCreateDTO) {
        UserModel user = new UserModel();
        user.setName(userCreateDTO.getName());
        user.setBirthdate(userCreateDTO.getBirthdate());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword());
        user.setUrlPhoto(userCreateDTO.getUrlPhoto());
        user.setRole(UserRole.USER); // Padrão como USER
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }

    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }



}
