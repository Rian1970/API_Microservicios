package com.himfg.permission.controller;

import com.himfg.permission.entity.Permission;
import com.himfg.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/permissions")
public class InternalPermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/{id}")
    public Permission getInternalPermissionById(@PathVariable Long id) {
        return permissionService.getInternalPermissionById(id);
    }
}
