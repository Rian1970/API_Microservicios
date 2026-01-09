package com.himfg.user.client.Permission;

import com.himfg.user.client.Permission.dto.GetPermission;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="permission-service", url = "http://permission-service:8084")
public interface PermissionClient {

    @GetMapping("/internal/permissions/{id}")
    GetPermission getInternalPermissionById(@PathVariable Long id);
}
