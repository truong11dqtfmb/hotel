package com.dqt.hotel.service;

import com.dqt.hotel.constant.Constant;
import com.dqt.hotel.dto.request.RoleRequest;
import com.dqt.hotel.dto.request.SignupRequest;
import com.dqt.hotel.dto.request.UserRequest;
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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        return ResponseMessage.ok("Created new user", new UserResponse(save));
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
        return ResponseMessage.ok("Change role user successfully", new UserResponse(save));
    }

    public ResponseMessage getCurrentUser() {
        User user = utils.gerCurrentUser();
        if (user == null) {
            return ResponseMessage.error("User not found");
        }
        return ResponseMessage.ok("Get current user", new UserResponse(user));
    }

    public Map<String, Object> getListUser(Integer page, Integer pageSize, String key) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, Constant.CREATED_AT));
        List<User> all = userRepository.filterUser(pageable, Utils.checkNullString(key));
        List<UserResponse> collect = all.stream().map(UserResponse::new).collect(Collectors.toList());

        Long count = userRepository.countUser(Utils.checkNullString(key));
        Map<String, Object> stringObjectMap = Utils.putData(page, pageSize, count, collect);

        return stringObjectMap;
    }


    public ResponseMessage addUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseMessage.error("User already registered");
        }

        Set<Role> roles = new HashSet<>();

        request.getRoles().forEach(r -> {
            Role role = roleRepository.findByName(r.toUpperCase()).orElseThrow(() -> new ResourceNotFoundException("Role", "roleName", "role"));
            roles.add(role);
        });

        // Create new user's account
        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .identity(request.getIdentity())
                .phone(request.getPhone())
                .address(request.getAddress())
                .password(encoder.encode(request.getPassword()))
                .roles(roles)
                .createAt(new Date())
                .enable(Constant.ACTIVE)
                .build();

        User save = userRepository.save(user);
        return ResponseMessage.ok("Created new user", new UserResponse(save));

    }

    public ResponseMessage getUserById(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("User not found");
        }
        User user = optional.get();
        return ResponseMessage.ok("Get user", new UserResponse(user));
    }

    public ResponseMessage editUser(UserRequest request, Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("User not found");
        }
        User user = optional.get();
        BeanUtils.copyProperties(request, user);
        User save = userRepository.save(user);
        return ResponseMessage.ok("Edit hotel successfully",new UserResponse(save));

    }

    public ResponseMessage enabled(Integer id, Integer active) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.error("User not found");
        }

        userRepository.updateEnabled(active, id);
        return ResponseMessage.ok("Successfully");
    }
}
