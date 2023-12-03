package com.madmotor.apimadmotordaw.rest.users.services;


import com.madmotor.apimadmotordaw.rest.users.dto.UserInfoResponse;
import com.madmotor.apimadmotordaw.rest.users.dto.UserRequest;
import com.madmotor.apimadmotordaw.rest.users.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UsersService {

    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UserInfoResponse findById(String id);

    UserResponse save(UserRequest userRequest);

    UserResponse update(String id, UserRequest userRequest);

    void deleteById(String id);

}
