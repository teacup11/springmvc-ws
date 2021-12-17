package com.appsdeveloperblog.app.ws.ui.controller;


import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String id) {

        UserRest returnValue = new UserRest();

        UserDTO userDto = userService.getUserByUserId(id);
        //BeanUtils.copyProperties(userDto, returnValue);
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserRest.class);


        return returnValue;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        UserRest returnValue = new UserRest();

        //UserDto userDto = new UserDto();
        //BeanUtils.copyProperties(userDetails, userDto);
        // better mapping for objects
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);

        UserDTO createdUser = userService.createUser(userDTO);
        //BeanUtils.copyProperties(createdUser, returnValue);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

        UserRest returnValue = new UserRest();

        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDTO updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name()); // we can hardcode it to "DELETE" or create enum

        // attempt to delete user details
        userService.deleteUser(id);

        // successful delete, return message
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    //pagination

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        // get list of users
        List<UserDTO> users = userService.getUsers(page, limit);

        //convert list back to user rest list
        for (UserDTO userDto : users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

    // get list of addresses for specific user endpoint
    // http://localhost:8080/mobile-app-ws/users/fjff9j9cj49je8/addresses
    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<AddressesRest> getUserAddresses(@PathVariable String id) {

        List<AddressesRest> returnValue = new ArrayList<>();

        List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

        if(addressesDTO != null && !addressesDTO.isEmpty()) {

            //use model mapper to map lists
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            returnValue = modelMapper.map(addressesDTO, listType);

        }

        return returnValue;
    }
}