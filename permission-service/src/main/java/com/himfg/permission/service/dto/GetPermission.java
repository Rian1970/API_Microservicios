package com.himfg.permission.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPermission {
    private Long permissionId;

    private String name;

    private String codename;

    private String description;

    private String application;
}
