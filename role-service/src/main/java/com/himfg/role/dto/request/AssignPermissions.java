package com.himfg.role.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class AssignPermissions {
    private Set<Long> permissionIds;
}
