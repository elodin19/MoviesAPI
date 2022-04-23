package com.cleverpy.moviesAPI.services.user;

import com.cleverpy.moviesAPI.dto.UserDto;
import com.cleverpy.moviesAPI.entities.Role;
import com.cleverpy.moviesAPI.entities.User;
import com.cleverpy.moviesAPI.repositories.RoleRepository;
import com.cleverpy.moviesAPI.repositories.UserRepository;
import com.cleverpy.moviesAPI.security.payload.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        encoder = mock(PasswordEncoder.class);
        underTest = new UserServiceImpl(repository, roleRepository, encoder);
    }

    @Test
    void createUser() {
        final UserDto dto = new UserDto("test@gmail.com", "test", "1234");
        final Role role = new Role(2L, "USER");

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        final ResponseEntity<?> expected = underTest.createUser(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(User.class);
        assertThat(expected.getBody()).asString().contains("email=test@gmail.com");
        assertThat(expected.getBody()).asString().contains("username=test");
    }

    @Test
    void shouldNotCreateDuplicatedUsername() {
        final UserDto dto = new UserDto("test@gmail.com", "test", "1234");
        final User user = new User(1L, "test@gmail.com", "test", encoder.encode("1234"), null);

        when(repository.findByUsername(dto.getUsername())).thenReturn(Optional.of(user));
        final ResponseEntity<?> expected = underTest.createUser(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The username " + dto.getUsername() + " is already being used");
    }

    @Test
    void shouldNotCreateDuplicatedEmail() {
        final UserDto dto = new UserDto("test@gmail.com", "test", "1234");
        final User user = new User(1L, "test@gmail.com", "test", encoder.encode("1234"), null);

        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        final ResponseEntity<?> expected = underTest.createUser(dto);

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The email " + dto.getEmail() + " is already being used");
    }

    @Test
    void getById() {
        final User user = new User(3L, "test@gmail.com", "test", encoder.encode("1234"), null);

        when(repository.findById(3L)).thenReturn(Optional.of(user));
        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        final ResponseEntity<?> expected = underTest.getById(3L, user.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(User.class);
        assertThat(expected.getBody()).asString().contains("id=3");
        assertThat(expected.getBody()).asString().contains("email=test@gmail.com");
        assertThat(expected.getBody()).asString().contains("username=test");
    }

    @Test
    void shouldNotGetById(){
        final Role role = new Role(1L, "USER");
        List<Role> roles = new ArrayList<Role>(){};
        roles.add(role);

        final User userAccessing = new User(1L, "another@user.com", "client", encoder.encode("1234"), roles);
        final User userToGet = new User(2L, "onemore@user.com", "client2", encoder.encode("1234"), roles);

        when(repository.findById(userToGet.getId())).thenReturn(Optional.of(userToGet));
        when(repository.findByUsername(userAccessing.getUsername())).thenReturn(Optional.of(userAccessing));
        final ResponseEntity<?> expected = underTest.getById(userToGet.getId(), userAccessing.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The user " + userAccessing.getUsername() + " is not allowed to get the user " + userToGet.getId());
    }

    @Test
    void getAllUsers() {
        final User user1 = new User(3L, "test@gmail.com", "test1",
                encoder.encode("1234"), null);
        final User user2 = new User(4L, "test@hotmail.com", "test2",
                encoder.encode("1234"), null);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        final Pageable page = PageRequest.of(0, 10);
        final Page<User> result = new PageImpl<>(users);

        when(repository.findAll(page)).thenReturn(result);
        ResponseEntity<?> expected = underTest.getAllUsers(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(Page.class);
    }

    @Test
    void getAllNull() {
        final Pageable page = PageRequest.of(0, 10);

        when(repository.findAll(page)).thenReturn(null);
        ResponseEntity<?> expected = underTest.getAllUsers(0);

        assertThat(expected.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    void updateUser() {
        final User user = new User(3L, "test@gmail.com", "test",
                encoder.encode("1234"), null);
        final UserDto dto = new UserDto("test@hotmail.com", "testing", "1234");

        when(repository.findById(3L)).thenReturn(Optional.of(user));
        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        final ResponseEntity<?> expected = underTest.updateUser(3L, dto, user.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(User.class);
        assertThat(expected.getBody()).asString().contains("id=3");
        assertThat(expected.getBody()).asString().contains("email=test@hotmail.com");
        assertThat(expected.getBody()).asString().contains("username=testing");
    }

    @Test
    void shouldNotUpdateNotOwnerNorAdmin(){
        final Role role = new Role(1L, "USER");
        List<Role> roles = new ArrayList<Role>(){};
        roles.add(role);

        final UserDto dto = new UserDto("test@gmail.com", "test", "1234");
        final User userAccessing = new User(1L, "another@user.com", "client", encoder.encode("1234"), roles);
        final User userToBeUpdated = new User(2L, "onemore@user.com", "client2", encoder.encode("1234"), null);

        when(repository.findById(userToBeUpdated.getId())).thenReturn(Optional.of(userToBeUpdated));
        when(repository.findByUsername(userAccessing.getUsername())).thenReturn(Optional.of(userAccessing));
        final ResponseEntity<?> expected = underTest.updateUser(userToBeUpdated.getId(), dto, userAccessing.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The user " + userAccessing.getUsername() + " is not allowed to update the user " + dto.getUsername());
    }

    @Test
    void shouldNotUpdateDuplicatedUsername() {
        final UserDto dto = new UserDto("test@gmail.com", "test", "1234");
        final User userAccessing = new User(1L, "manager@gmail.com", "manager", encoder.encode("1234"), null);

        when(repository.existsByUsername(dto.getUsername())).thenReturn(true);
        when(repository.findById(userAccessing.getId())).thenReturn(Optional.of(userAccessing));
        when(repository.findByUsername(userAccessing.getUsername())).thenReturn(Optional.of(userAccessing));
        final ResponseEntity<?> expected = underTest.updateUser(userAccessing.getId(), dto, userAccessing.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("This username is already being used");
    }

    @Test
    void shouldNotUpdateDuplicatedEmail(){
        final UserDto dto = new UserDto("test@gmail.com", "test", "1234");
        final User userAccessing = new User(1L, "manager@gmail.com", "manager", encoder.encode("1234"), null);

        when(repository.existsByEmail(dto.getEmail())).thenReturn(true);
        when(repository.findById(userAccessing.getId())).thenReturn(Optional.of(userAccessing));
        when(repository.findByUsername(userAccessing.getUsername())).thenReturn(Optional.of(userAccessing));
        final ResponseEntity<?> expected = underTest.updateUser(userAccessing.getId(), dto, userAccessing.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("This email is already being used");
    }

    @Test
    void delete() {
        final User user = new User(3L, "test@gmail.com", "test", encoder.encode("1234"), null);

        when(repository.findById(3L)).thenReturn(Optional.of(user));
        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        final ResponseEntity<?> expected = underTest.deleteUser(3L, user.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("User " + user.getId() + " deleted with success");
    }

    @Test
    void shouldNotDelete(){
        final Role role = new Role(1L, "USER");
        List<Role> roles = new ArrayList<Role>(){};
        roles.add(role);

        final User userConnecting = new User(3L, "test@gmail.com", "test", encoder.encode("1234"), roles);
        final User userToBeRemoved = new User(4L, "tobe@removed.com", "remove", encoder.encode("1234"), null);

        when(repository.findById(userToBeRemoved.getId())).thenReturn(Optional.of(userToBeRemoved));
        when(repository.findByUsername(userConnecting.getUsername())).thenReturn(Optional.of(userConnecting));
        final ResponseEntity<?> expected = underTest.deleteUser(userToBeRemoved.getId(), userConnecting.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(400);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("The user " + userConnecting.getUsername() + " is not allowed to delete the user " + userToBeRemoved.getUsername());
    }
}