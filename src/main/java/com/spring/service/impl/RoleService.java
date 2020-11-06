package com.spring.service.impl;

import com.spring.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getRoleSet(Set<String> roles);
    Role getDefaultRole();

}
