package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.RoleRequest;
import com.dqt.hotel.dto.request.SignupRequest;
import com.dqt.hotel.dto.response.ResponseMessage;
import com.dqt.hotel.dto.response.UserResponse;
import com.dqt.hotel.entity.Role;
import com.dqt.hotel.entity.User;
import com.dqt.hotel.exception.ResourceNotFoundException;
import com.dqt.hotel.repository.RoleRepository;
import com.dqt.hotel.repository.UserRepository;
import com.dqt.hotel.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final Utils utils;

    public ResponseMessage signUp(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseMessage.error("User already registered");
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Constant.ROLE_USER).orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", "role"));
        roles.add(userRole);

        // Create new user's account
        User user = User.builder()
                .email(signUpRequest.getEmail())
                .fullName(signUpRequest.getFullName())
                .identity(signUpRequest.getIdentity())
                .phone(signUpRequest.getPhone())
                .address(signUpRequest.getAddress())
                .password(encoder.encode(signUpRequest.getPassword()))
                .roles(roles)
                .createAt(new Date())
                .enable(Constant.ACTIVE)
                .build();
        User save = userRepository.save(user);
        return ResponseMessage.ok("Created new user", convertToUserResponse(save));
    }

    public ResponseMessage changeRole(RoleRequest roleRequest) {
        Optional<User> optional = userRepository.findById(roleRequest.getUserId());
        if (!optional.isPresent()) {
            return ResponseMessage.error("User not found");
        }
        User user = optional.get();

        Set<Role> roles = new HashSet<>();

        roleRequest.getRoles().forEach(r -> {
            Role role = roleRepository.findByName(r.toUpperCase()).orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", "role"));
            roles.add(role);
        });

        user.setRoles(roles);
        User save = userRepository.save(user);
        return ResponseMessage.ok("Change role user successfully", convertToUserResponse(save));
    }

    public ResponseMessage getCurrentUser() {
        User user = utils.gerCurrentUser();
        if (user == null) {
            return ResponseMessage.error("User not found");
        }
        return ResponseMessage.ok("Get current user", convertToUserResponse(user));
    }

    public UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .address(user.getAddress())
                .phone(user.getPhone())
                .identity(user.getIdentity())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .enable(user.getEnable())
                .build();
    }


}
