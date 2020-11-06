package com.spring.controller.simple;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/**")
public class AdminController {

    @GetMapping("users")
    public ModelAndView viewAdminCP () {
        return new ModelAndView("/admin/users");
    }

}
