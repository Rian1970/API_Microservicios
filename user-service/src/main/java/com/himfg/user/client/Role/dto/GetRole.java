package com.himfg.user.client.Role.dto;

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
    private Set<Long> permissionIds;
}
