package com.madmotor.apimadmotordaw.rest.users.services;
import com.madmotor.apimadmotordaw.rest.pedidos.repositories.PedidoRepository;
import com.madmotor.apimadmotordaw.rest.users.dto.UserInfoResponse;
import com.madmotor.apimadmotordaw.rest.users.dto.UserRequest;
import com.madmotor.apimadmotordaw.rest.users.dto.UserResponse;
import com.madmotor.apimadmotordaw.rest.users.exceptions.UserNameOrEmailExists;
import com.madmotor.apimadmotordaw.rest.users.exceptions.UserNotFound;
import com.madmotor.apimadmotordaw.rest.users.mappers.UsersMapper;
import com.madmotor.apimadmotordaw.rest.users.models.User;
import com.madmotor.apimadmotordaw.rest.users.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplTest {

    private final UserRequest userRequest = UserRequest.builder().username("test").email("test@test.com").build();
    private final User user = User.builder().id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")).username("test").email("test@test.com").build();
    private final UserResponse userResponse = UserResponse.builder().username("test").email("test@test.com").build();
    private final UserInfoResponse userIResponse = UserInfoResponse.builder().username("test").email("test@test.com").build();
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PedidoRepository pedidosRepository;
    @Mock
    private UsersMapper usersMapper;
    @InjectMocks
    private UsersServiceImpl usersService;


    @Test
    public void testFindAll_NoFilters_ReturnsPageOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        Page<User> page = new PageImpl<>(users);
        when(usersRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(usersMapper.toUserResponse(any(User.class))).thenReturn(new UserResponse());

        Page<UserResponse> result = usersService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements())
        );

        // Verify
        verify(usersRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void testFindById() {
        UUID id= UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
        when(pedidosRepository.findPedidosIdsByIdUsuario(id)).thenReturn(List.of());
        when(usersMapper.toUserInfoResponse(any(User.class), anyList())).thenReturn(userIResponse);


        UserInfoResponse result = usersService.findById(id.toString());

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userResponse.getUsername(), result.getUsername()),
                () -> assertEquals(userResponse.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findById(id);
        verify(pedidosRepository, times(1)).findPedidosIdsByIdUsuario(id);
        verify(usersMapper, times(1)).toUserInfoResponse(user, List.of());

    }

    @Test
    public void testFindById_UserNotFound_ThrowsUserNotFound() {
        UUID id= UUID.fromString("123e4567-e89b-12d3-a456-426617174000");
        when(usersRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserNotFound.class, () -> usersService.findById(id.toString()));

        // Verify
        verify(usersRepository, times(1)).findById(id);
    }

    @Test
    public void testSave_ValidUserRequest_ReturnsUserResponse() {
        // Arrange


        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        // Act
        UserResponse result = usersService.save(userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);

    }

    @Test
    public void testSave_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
        // Arrange
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(new User()));

        // Act and Assert
        assertThrows(UserNameOrEmailExists.class, () -> usersService.save(userRequest));
    }

    @Test
    public void testUpdate_ValidUserRequest_ReturnsUserResponse() {
        // Arrange
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(usersMapper.toUser(userRequest, userId)).thenReturn(user);
        when(usersMapper.toUserResponse(user)).thenReturn(userResponse);
        when(usersRepository.save(user)).thenReturn(user);

        // Act
        UserResponse result = usersService.update(userId.toString(), userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(userRequest.getUsername(), result.getUsername()),
                () -> assertEquals(userRequest.getEmail(), result.getEmail())
        );

        // Verify
        verify(usersRepository, times(1)).findById(userId);
        verify(usersRepository, times(1)).findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString());
        verify(usersMapper, times(1)).toUser(userRequest, userId);
        verify(usersMapper, times(1)).toUserResponse(user);
        verify(usersRepository, times(1)).save(user);
    }

    @Test
    public void testUpdate_DuplicateUsernameOrEmail_ThrowsUserNameOrEmailExists() {
        // Arrange
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(UserNameOrEmailExists.class, () -> usersService.update(userId.toString(), userRequest));
    }



    @Test
    public void testDeleteById_PhisicalDelete() {
        // Arrange
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(false);

        // Act
        usersService.deleteById(userId.toString());

        // Verify
        verify(usersRepository, times(1)).delete(user);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    public void testDeleteById_LogicalDelete() {
        // Arrange
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(true);
        doNothing().when(usersRepository).updateIsDeletedToTrueById(userId);

        // Act
        usersService.deleteById(userId.toString());

        // Assert

        // Verify
        verify(usersRepository, times(1)).updateIsDeletedToTrueById(userId);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }

    @Test
    public void testDeleteByIdNotExists() {
        // Arrange
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());
        User user = new User();
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(pedidosRepository.existsByIdUsuario(userId)).thenReturn(true);

        usersService.deleteById(userId.toString());

        verify(usersRepository, times(1)).updateIsDeletedToTrueById(userId);
        verify(pedidosRepository, times(1)).existsByIdUsuario(userId);
    }
}