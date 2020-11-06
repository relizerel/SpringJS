package com.spring.service.impl;


import com.spring.model.User;
import com.spring.repository.UserRepo;
import com.spring.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepository;

    @Autowired
    public UserServiceImpl(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getOne(id);
    }


    @Override
    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            user = userRepository.findUserByName(userDetails.getUsername());
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userFindByUserName = userRepository.findUserByName(s);
        User userFindByUserEmail = userRepository.findUserByEmail(s);

        if (userFindByUserName != null) {
            return userFindByUserName;
        }

        if (userFindByUserEmail != null) {
            return userFindByUserEmail;
        }

        return null;
    }
}