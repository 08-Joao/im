package com.im.BackendCore.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.im.BackendCore.User.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    // GET /user/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok) // 200 com corpo
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404
    }

    // POST /user/create
    @PostMapping("/create")
    public ResponseEntity<UserModel> createUser(@RequestBody UserCreateDTO userCreateDTO) {
        UserModel createdUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(201).body(createdUser); // 201 Created
    }

    // DELETE /user/deleteById/{id}
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUserById(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204
        } else {
            return ResponseEntity.notFound().build(); // 404
        }
    }
}