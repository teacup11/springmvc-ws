package com.appsdeveloperblog.app.ws.ui.controller;

import com.appsdeveloperblog.app.ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDTO userDTO;

    final String USER_ID = "d9jva09v90svd3432d";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDTO = new UserDTO();
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setFirstName("Mojca");
        userDTO.setLastName("Pokraculja");
        userDTO.setPassword("8de9c8893");
        userDTO.setEmail("test@test.com");
        userDTO.setUserId(USER_ID);
        userDTO.setEncryptedPassword("s8uv99uv90");
    }

    @Test
    void testGetUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDTO);

        UserRest userRest = userController.getUser(USER_ID);
        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDTO.getFirstName(), userRest.getFirstName());
        assertEquals(userDTO.getLastName(), userRest.getLastName());
        assertTrue(userDTO.getAddresses().size() == userRest.getAddresses().size());
    }

    private List<AddressDTO> getAddressesDTO() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setType("shipping");
        addressDTO.setCity("Ljubljana");
        addressDTO.setCountry("Slovenia");
        addressDTO.setPostalCode("1000");
        addressDTO.setStreetName("Ulica 123");

        AddressDTO billingAddressDTO = new AddressDTO();
        billingAddressDTO.setType("billing");
        billingAddressDTO.setCity("Ljubljana");
        billingAddressDTO.setCountry("Slovenia");
        billingAddressDTO.setPostalCode("1000");
        billingAddressDTO.setStreetName("Ulica 345");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDTO);
        addresses.add(billingAddressDTO);

        return addresses;
    }
}