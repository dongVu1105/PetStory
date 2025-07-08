package com.dongVu1105.identity_service.service;

import com.dongVu1105.identity_service.constant.PredefinedRole;
import com.dongVu1105.identity_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.ProfileCreationResponse;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import com.dongVu1105.identity_service.entity.Role;
import com.dongVu1105.identity_service.entity.User;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.mapper.ProfileMapper;
import com.dongVu1105.identity_service.mapper.UserMapper;
import com.dongVu1105.identity_service.repository.RoleRepository;
import com.dongVu1105.identity_service.repository.UserRepository;
import com.dongVu1105.identity_service.repository.httpclient.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
//    ProfileClient profileClient;
//    ProfileMapper profileMapper;
    RoleRepository roleRepository;

    public UserResponse create (UserCreationRequest request) throws AppException {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById(PredefinedRole.USER_ROLE).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_EXISTED)));
        user.setRoles(roles);

//        ProfileCreationRequest profileCreationRequest = profileMapper.toProfileCreationRequest(request);
//        profileCreationRequest.setUserID(user.getId());
//        ProfileCreationResponse profileCreationResponse = profileClient.create(profileCreationRequest).getResult();

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        UserResponse userResponse = userMapper.toUserCreationResponse(user);
//        userResponse.setId(profileCreationResponse.getId());
        return userResponse;
    }



}
