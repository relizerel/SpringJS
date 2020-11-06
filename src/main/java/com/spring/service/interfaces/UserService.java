package com.spring.service.interfaces;


import com.spring.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    void addUser(User user);
    List<User> listUsers();
    void deleteUser(Long id);
    void updateUser(User user);
    User getUserById(Long id);
    User getAuthUser();
    User findUserByEmail(String email);

    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
}
