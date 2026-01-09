package com.himfg.user.client.Role;

import com.himfg.user.client.Role.dto.GetRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="role-service", url = "http://role-service:8085")
public interface RoleClient {

    @GetMapping("/internal/roles/{id}")
    GetRole getInternalRoleById(@PathVariable Long id);
}
