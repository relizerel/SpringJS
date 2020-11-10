package com.spring.repository;

import com.spring.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Set<Role> getRolesByNameIn(Set<String> roles);
    Role getRoleByName(String defaultRoleName);
}