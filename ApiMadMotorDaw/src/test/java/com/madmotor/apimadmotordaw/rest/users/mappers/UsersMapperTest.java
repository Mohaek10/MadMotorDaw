package com.madmotor.apimadmotordaw.rest.users.mappers;

import com.madmotor.apimadmotordaw.rest.users.dto.UserInfoResponse;
import com.madmotor.apimadmotordaw.rest.users.dto.UserRequest;
import com.madmotor.apimadmotordaw.rest.users.dto.UserResponse;
import com.madmotor.apimadmotordaw.rest.users.mappers.UsersMapper;
import com.madmotor.apimadmotordaw.rest.users.models.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class UsersMapperTest {

    @InjectMocks
    private UsersMapper usersMapper;

    public UsersMapperTest() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testToUser() {
        UserRequest userRequest = mock(UserRequest.class);

        User user = usersMapper.toUser(userRequest);
        assertAll(
                () -> assertEquals(userRequest.getNombre(), user.getNombre()),
                () -> assertEquals(userRequest.getApellidos(), user.getApellidos()),
                () -> assertEquals(userRequest.getUsername(), user.getUsername()),
                () -> assertEquals(userRequest.getEmail(), user.getEmail()),
                () -> assertEquals(userRequest.getPassword(), user.getPassword()),
                () -> assertEquals(userRequest.getRoles(), user.getRoles()),
                () -> assertEquals(userRequest.getIsDeleted(), user.getIsDeleted())
        );

    }

    @Test
    public void testToUserWithId() {
        UserRequest userRequest = mock(UserRequest.class);
        UUID id = UUID.randomUUID();

        User user = usersMapper.toUser(userRequest, id);

        assertAll(
                () -> assertEquals(id, user.getId()),
                () -> assertEquals(userRequest.getNombre(), user.getNombre()),
                () -> assertEquals(userRequest.getApellidos(), user.getApellidos()),
                () -> assertEquals(userRequest.getUsername(), user.getUsername()),
                () -> assertEquals(userRequest.getEmail(), user.getEmail()),
                () -> assertEquals(userRequest.getPassword(), user.getPassword()),
                () -> assertEquals(userRequest.getRoles(), user.getRoles()),
                () -> assertEquals(userRequest.getIsDeleted(), user.getIsDeleted()));
    }

    @Test
    public void testToUserResponse() {
        User user=new User();
        user.setId(UUID.randomUUID());
        UserResponse userResponse = usersMapper.toUserResponse(user);
        assertAll(
                () -> assertEquals(user.getId().toString(), userResponse.getId()),
                () -> assertEquals(user.getNombre(), userResponse.getNombre()),
                () -> assertEquals(user.getApellidos(), userResponse.getApellidos()),
                () -> assertEquals(user.getUsername(), userResponse.getUsername()),
                () -> assertEquals(user.getEmail(), userResponse.getEmail()),
                () -> assertEquals(user.getRoles(), userResponse.getRoles()),
                () -> assertEquals(user.getIsDeleted(), userResponse.getIsDeleted()));

    }

    @Test
    public void testToUserInfoResponse() {
        User user=new User();
        user.setId(UUID.randomUUID());
        List<String> pedidos = Collections.singletonList("Pedido1");

        UserInfoResponse userInfoResponse = usersMapper.toUserInfoResponse(user, pedidos);
        assertAll(
                () -> assertEquals(user.getId().toString(), userInfoResponse.getId()),
                () -> assertEquals(user.getNombre(), userInfoResponse.getNombre()),
                () -> assertEquals(user.getApellidos(), userInfoResponse.getApellidos()),
                () -> assertEquals(user.getUsername(), userInfoResponse.getUsername()),
                () -> assertEquals(user.getEmail(), userInfoResponse.getEmail()),
                () -> assertEquals(user.getRoles(), userInfoResponse.getRoles()),
                () -> assertEquals(user.getIsDeleted(), userInfoResponse.getIsDeleted()),
                () -> assertEquals(pedidos, userInfoResponse.getPedidos()));


    }
}
