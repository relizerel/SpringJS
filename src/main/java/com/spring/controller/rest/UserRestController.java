package com.spring.controller.rest;

import com.spring.model.dto.DTOUser;
import com.spring.service.impl.DTOService;
import com.spring.service.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/**")
public class UserRestController {

    final DTOService converter;
    final UserService userService;

    public UserRestController(DTOService converter, UserService userService) {
        this.converter = converter;
        this.userService = userService;
    }


    @GetMapping("authUser")
    public ResponseEntity<DTOUser> getAuthUser() {
        DTOUser dtoUser = converter.getAuthUserInfo(userService.getAuthUser());
        return dtoUser != null
                ? new ResponseEntity<>(dtoUser, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
