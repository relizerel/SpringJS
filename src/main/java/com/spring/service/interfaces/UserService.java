package com.spring.service.interfaces;


import com.spring.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void addUser(User user);
    List<User> getAllUsers();
    void deleteUser(Long id);
    void updateUser(User user);
    User getUserById(Long id);
    User getAuthUser();
    User findUserByEmail(String email);
}
