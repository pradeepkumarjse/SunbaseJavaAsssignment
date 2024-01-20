package com.sunbase.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sunbase.entity.Role;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

	Role findByRoleName(String roleName);
}