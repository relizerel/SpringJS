package com.spring.controller.simple;

import com.spring.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/**")
public class UserController {

    @GetMapping("userPage")
    public String showUserForm(@AuthenticationPrincipal User user) {
        return "/user/userPage";
    }
}
