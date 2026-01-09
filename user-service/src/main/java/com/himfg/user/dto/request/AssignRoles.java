package com.himfg.user.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class AssignRoles {
    private Set<Long> roleIds;
}
