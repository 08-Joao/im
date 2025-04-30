package com.im.BackendCore.User;

import com.im.BackendCore.Enums.UserRole;
import com.im.BackendCore.Exception.AuthenticationException;
import com.im.BackendCore.Exception.BadRequestException;
import com.im.BackendCore.Exception.ResourceNotFoundException;
import com.im.BackendCore.Utils.CpfFormatter;
import com.im.BackendCore.Utils.ValidateCpf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Masks a CPF for logging purposes, showing only the first 3 and last 2 digits
     * Example: 123.456.789-00 becomes 123.***.***-00
     */
    private String maskCpf(String cpf) {
        if (cpf == null || cpf.length() < 11) {
            return "invalid-cpf";
        }
        String cleanCpf = cpf.replaceAll("\\D", "");
        if (cleanCpf.length() < 11) {
            return "invalid-cpf";
        }
        return cleanCpf.substring(0, 3) + ".***.***-" + cleanCpf.substring(9, 11);
    }

    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserModel createUser(UserCreateDTO userCreateDTO) {
        try {
            logger.info("Attempting to create new user with email: {}", userCreateDTO.getEmail());

            // Validate required fields
            if (userCreateDTO.getName() == null || userCreateDTO.getName().trim().isEmpty()) {
                logger.warn("User creation failed: Name is required");
                throw new BadRequestException("Name is required");
            }

            if (userCreateDTO.getEmail() == null || userCreateDTO.getEmail().trim().isEmpty()) {
                logger.warn("User creation failed: Email is required");
                throw new BadRequestException("Email is required");
            }

            if (userCreateDTO.getPassword() == null || userCreateDTO.getPassword().trim().isEmpty()) {
                logger.warn("User creation failed: Password is required");
                throw new BadRequestException("Password is required");
            }

            if (userCreateDTO.getCpf() == null || userCreateDTO.getCpf().trim().isEmpty()) {
                logger.warn("User creation failed: CPF is required");
                throw new BadRequestException("CPF is required");
            }

            String formattedCpf = CpfFormatter.format(userCreateDTO.getCpf());
            userCreateDTO.setCpf(formattedCpf);

            // Validate the CPF
            if(!ValidateCpf.isValid(userCreateDTO.getCpf())) {
                logger.warn("User creation failed: Invalid CPF format for CPF: {}", maskCpf(userCreateDTO.getCpf()));
                throw new BadRequestException("Invalid CPF format");
            }

            // Check if email already exists
            if (userRepository.findByEmail(userCreateDTO.getEmail()).isPresent()) {
                logger.warn("User creation failed: Email already registered: {}", userCreateDTO.getEmail());
                throw new BadRequestException("Email already registered");
            }

            // Check if CPF already exists
            if (userRepository.findByCpf(userCreateDTO.getCpf()).isPresent()) {
                logger.warn("User creation failed: CPF already registered: {}", maskCpf(userCreateDTO.getCpf()));
                throw new BadRequestException("CPF already registered");
            }

            UserModel user = new UserModel();
            user.setName(userCreateDTO.getName());
            user.setBirthdate(userCreateDTO.getBirthdate());
            user.setCpf(userCreateDTO.getCpf());
            user.setEmail(userCreateDTO.getEmail());

            // Enhanced password security - ensure it meets minimum requirements
            String password = userCreateDTO.getPassword();
            if (password.length() < 8) {
                logger.warn("User creation failed: Password too short");
                throw new BadRequestException("Password must be at least 8 characters long");
            }

            user.setPassword(passwordEncoder.encode(password));
            user.setUrlPhoto(userCreateDTO.getUrlPhoto());
            user.setRole(UserRole.USER);
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());

            UserModel savedUser = userRepository.save(user);
            logger.info("User created successfully with ID: {}", savedUser.getId());
            return savedUser;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating user: {}", e.getMessage(), e);
            throw new BadRequestException("Error creating user: " + e.getMessage());
        }
    }

    public UserModel editUser(Long id, UserEditDTO userEditDTO) {
        try {
            logger.info("Attempting to edit user with ID: {}", id);

            Optional<UserModel> optionalUser = userRepository.findById(id);
            if(optionalUser.isEmpty()){
                logger.warn("User edit failed: User not found with ID: {}", id);
                throw new ResourceNotFoundException("User not found with ID: " + id);
            }

            UserModel user = optionalUser.get();

            // Handle email updates and check for uniqueness
            if(userEditDTO.getEmail() != null && !userEditDTO.getEmail().equals(user.getEmail())) {
                logger.info("Attempting to update email for user ID {}: {} -> {}", id, user.getEmail(), userEditDTO.getEmail());

                Optional<UserModel> existingUserWithEmail = userRepository.findByEmail(userEditDTO.getEmail());
                if(existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) {
                    logger.warn("User edit failed: Email already registered to another user: {}", userEditDTO.getEmail());
                    throw new BadRequestException("Email already registered to another user");
                }
                user.setEmail(userEditDTO.getEmail());
            }

            if(userEditDTO.getName() != null) {
                user.setName(userEditDTO.getName());
            }

            if(userEditDTO.getBirthdate() != null) {
                user.setBirthdate(userEditDTO.getBirthdate());
            }

            if(userEditDTO.getUrlPhoto() != null) {
                user.setUrlPhoto(userEditDTO.getUrlPhoto());
            }

            // Handle password updates
            String currPassword = user.getPassword();
            String newPassword = userEditDTO.getNewPassword();

            if(newPassword != null && !newPassword.isEmpty()) {
                logger.info("Password change requested for user ID: {}", id);

                String userEditCurrPassword = userEditDTO.getPassword();

                if(userEditCurrPassword == null || userEditCurrPassword.isEmpty()) {
                    logger.warn("Password change failed: Current password not provided for user ID: {}", id);
                    throw new BadRequestException("Current password is required to set a new password");
                }

                if(!passwordEncoder.matches(userEditCurrPassword, currPassword)){
                    logger.warn("Password change failed: Incorrect current password for user ID: {}", id);
                    throw new AuthenticationException("Invalid password. Current password is incorrect.");
                }

                // Validate new password strength
                if (newPassword.length() < 8) {
                    logger.warn("Password change failed: New password too short for user ID: {}", id);
                    throw new BadRequestException("New password must be at least 8 characters long");
                }

                user.setPassword(passwordEncoder.encode(newPassword));
                logger.info("Password successfully changed for user ID: {}", id);
            }

            user.setUpdatedAt(new Date());

            UserModel savedUser = userRepository.save(user);
            logger.info("User updated successfully with ID: {}", id);
            return savedUser;
        } catch (ResourceNotFoundException | BadRequestException | AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating user ID {}: {}", id, e.getMessage(), e);
            throw new BadRequestException("Error updating user: " + e.getMessage());
        }
    }

    public boolean deleteUserById(Long id) {
        try {
            logger.info("Attempting to delete user with ID: {}", id);

            if (!userRepository.existsById(id)) {
                logger.warn("User deletion failed: User not found with ID: {}", id);
                throw new ResourceNotFoundException("User not found with ID: " + id);
            }

            userRepository.deleteById(id);
            logger.info("User with ID: {} successfully deleted", id);
            return true;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting user ID {}: {}", id, e.getMessage(), e);
            throw new BadRequestException("Error deleting user: " + e.getMessage());
        }
    }
}