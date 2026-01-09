package com.himfg.user.client.Permission.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetPermission {
    private Long permissionId;
    private String name;
    private String codename;
}
