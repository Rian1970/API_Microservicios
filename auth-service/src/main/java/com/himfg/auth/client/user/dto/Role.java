package com.himfg.auth.client.user.dto;

import lombok.Data;

@Data
public class Role {
    private Long roleId;
    private String name;
    private String codename;
}