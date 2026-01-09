package com.himfg.role.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class GetRole {
    private Long roleId;

    private String name;

    private String codename;

    private String description;

    private String notes;

    private String application;

    private Set<Long> permissionIds;
}
