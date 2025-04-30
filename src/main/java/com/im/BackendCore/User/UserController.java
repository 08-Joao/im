package com.im.BackendCore.User;

import com.im.BackendCore.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/ping")
    public String teste(){
        return "Pong";
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(UserResponseDTO.fromUserModel(user)))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserModel createdUser = userService.createUser(userCreateDTO);
        UserResponseDTO responseDTO = UserResponseDTO.fromUserModel(createdUser);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<UserResponseDTO> editUser(@PathVariable Long id, @RequestBody UserEditDTO userEditDTO) {
        UserModel editedUser = userService.editUser(id, userEditDTO);
        UserResponseDTO responseDTO = UserResponseDTO.fromUserModel(editedUser);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}