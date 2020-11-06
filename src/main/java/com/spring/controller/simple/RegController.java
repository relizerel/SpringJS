package com.spring.controller.simple;

import com.spring.model.User;
import com.spring.service.interfaces.RoleService;
import com.spring.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reg")
public class RegController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RegController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showRegistrationForm(User user) {
        return "reg";
    }

    @PostMapping("/newuser")
    public String createUser(User user) {
        user.getRoleSet().add(roleService.getDefaultRole());
        userService.addUser(user);
        return "redirect:/login";
    }

}
