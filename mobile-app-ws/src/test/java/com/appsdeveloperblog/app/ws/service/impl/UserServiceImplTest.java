package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void testGetUser() {

        UserEntity userEntity = new UserEntity();
        // hardcode the values that i want it to have
        userEntity.setId(1L);
        userEntity.setFirstName("Mojca");
        userEntity.setUserId("hd74i4cj49");
        userEntity.setEncryptedPassword("9d8d7d6d5d");

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO = userService.getUser("test@test.com");

        assertNotNull(userDTO);
        assertEquals("Mojca", userDTO.getFirstName());

    }

}