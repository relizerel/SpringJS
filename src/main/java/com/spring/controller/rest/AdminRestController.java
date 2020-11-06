package com.spring.controller.rest;

import com.spring.model.dto.DTOUser;
import com.spring.service.interfaces.DTOService;
import com.spring.service.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("admin/**")
public class AdminRestController {

    private final UserService userService;
    private final DTOService converter;

    public AdminRestController(UserService userService, DTOService converter) {
        this.userService = userService;
        this.converter = converter;
    }

    @GetMapping("userList")
    public ResponseEntity<List<DTOUser>> listOfUsers() {
        List<DTOUser> dtoUsers = converter.userListConvertToDTO(userService.listUsers());
        return dtoUsers != null && !dtoUsers.isEmpty()
                ? new ResponseEntity<>(dtoUsers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<DTOUser> getUserById(@PathVariable(name = "id") Long id) {
        DTOUser dtoUser = converter.userConvertToDTOUser(userService.getUserById(id));
        return dtoUser != null
                ? new ResponseEntity<>(dtoUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("newUser")
    public ResponseEntity<DTOUser> newUser(@RequestBody DTOUser dtoUser) {
        try {
            userService.addUser(converter.dtoUserConvertToUser(dtoUser));
            return new ResponseEntity<>(dtoUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("updateUser")
    public ResponseEntity<DTOUser> updateUser(@RequestBody DTOUser dtoUser) {
        try {
            userService.updateUser(converter.dtoUserConvertToUser(dtoUser));
            return new ResponseEntity<>(dtoUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
