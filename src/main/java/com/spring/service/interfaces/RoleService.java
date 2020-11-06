package com.spring.service.interfaces;

import com.spring.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getRoleSet(Set<String> roles);
    Role getDefaultRole();

}
