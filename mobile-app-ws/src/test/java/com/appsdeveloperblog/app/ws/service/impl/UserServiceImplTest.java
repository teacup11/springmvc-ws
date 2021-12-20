package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //declare stuff on class level, beacuse we use it more than once here
    String userId = "jds8909dsj9e";
    String encryptedPassword = "9d8d7d6d5d";

    UserEntity userEntity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity(); // zakaj je tuki še enkrat če je že on class level
        // hardcode the values that i want it to have
        userEntity.setId(1L);
        userEntity.setFirstName("Mojca");
        userEntity.setLastName("Pokraculja");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@test.com");
        userEntity.setAddresses(getAddressesEntity());

    }

    // TESTS GET USER METHOD

    @Test
    final void testGetUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO = userService.getUser("test@test.com");

        assertNotNull(userDTO);
        assertEquals("Mojca", userDTO.getFirstName());

    }

    //Expect an exception user name not found
    @Test
    final void testGetUser_UsernameNotFoundException() {

        // when find by email is called on user repository, and it returns null
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,

                //lambda expression
                ()-> {
                    userService.getUser("test@test.com");
                }

                );
    }

    // TESTS CREATE USER METHOD

    //make find by email method return null so that we can create user
    @Test
    final void testCreateUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("dft6789olmnbgt78");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        // !!!FIX THIS!!!
        //when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);


        // create addresses was moved to separate method

        UserDTO userDTO = new UserDTO();
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setFirstName("Mojca");
        userDTO.setLastName("Pokraculja");
        userDTO.setPassword("8de9c8893");
        userDTO.setEmail("test@test.com");

        UserDTO storedUserDetails = userService.createUser(userDTO);
        //check values
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());

        verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("8de9c8893");
        //verify(userRepository, times(1)).save(any(UserEntity.class));

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

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDTO> addresses = getAddressesDTO();

        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();

        return new ModelMapper().map(addresses, listType);
    }


}