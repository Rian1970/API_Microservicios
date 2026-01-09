package com.himfg.role.controller;

import com.himfg.role.entity.Role;
import com.himfg.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/roles")
public class InternalRoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/{id}")
    public Role getInternalRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }
}
