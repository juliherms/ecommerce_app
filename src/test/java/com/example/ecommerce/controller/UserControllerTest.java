package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CreateUserRequestDTO;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repositories.CartRepository;
import com.example.ecommerce.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.ecommerce.utils.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();

        injectObjects(userController, "userRepository", userRepository);
        injectObjects(userController, "cartRepository", cartRepository);
        injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserWithPasswordNotMatchError(){

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequestDTO request = new CreateUserRequestDTO();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword2");

        ResponseEntity<?> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void createUserWithPasswordNotMinCharacterError(){

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequestDTO request = new CreateUserRequestDTO();
        request.setUsername("test");
        request.setPassword("123");
        request.setConfirmPassword("123");

        ResponseEntity<?> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

    }

    @Test
    public void createUserAndGenerateYourHashSuccess(){

        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");

        CreateUserRequestDTO request = new CreateUserRequestDTO();
        request.setUsername("test");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        ResponseEntity<?> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = (User) response.getBody();

        assertNotNull(user);

        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void verifyUserByIdSuccess(){

        long id = 1L;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User actualUser = response.getBody();

        assertNotNull(actualUser);

        assertEquals(id, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
    }

    @Test
    public void verifyUserByNameSuccess(){

        long id = 1L;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);

        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User actualUser = response.getBody();

        assertNotNull(actualUser);

        assertEquals(id, actualUser.getId());
        assertEquals("test", actualUser.getUsername());
        assertEquals("testPassword", actualUser.getPassword());
    }

}
