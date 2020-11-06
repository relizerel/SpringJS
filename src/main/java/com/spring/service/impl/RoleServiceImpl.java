package com.spring.service.impl;

import com.spring.model.Role;
import com.spring.repository.RoleRepo;
import com.spring.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepository) {
        this.roleRepo = roleRepository;
    }

    @Override
    public Set<Role> getRoleSet(Set<String> roles) {
        return new HashSet<>(roleRepo.getRolesByNameIn(roles));
    }

    @Override
    public Role getDefaultRole() {
        String defaultRoleName = "USER";
        return roleRepo.getRoleByName(defaultRoleName);
    }
}
