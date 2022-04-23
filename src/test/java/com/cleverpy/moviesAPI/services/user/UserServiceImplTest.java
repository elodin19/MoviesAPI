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
    void getById() {
        final User user = new User(3L, "test@gmail.com", "test",
                encoder.encode("1234"), null);

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
    void deleteUser() {
        final User user = new User(3L, "test@gmail.com", "test",
                encoder.encode("1234"), null);

        when(repository.findById(3L)).thenReturn(Optional.of(user));
        when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        final ResponseEntity<?> expected = underTest.deleteUser(3L, user.getUsername());

        assertThat(expected.getStatusCodeValue()).isEqualTo(200);
        assertThat(expected.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(expected.getBody()).asString().contains("User " + user.getId() + " deleted with success");
    }
}