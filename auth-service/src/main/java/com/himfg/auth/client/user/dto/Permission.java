package com.himfg.auth.client.user.dto;

import lombok.Data;

@Data
public class Permission {
    private Long permissionId;
    private String name;
    private String codename;
}