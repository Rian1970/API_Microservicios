package com.himfg.auth.client.user;

import com.himfg.auth.client.user.dto.CreateUser;
import com.himfg.auth.client.user.dto.GetUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="user-service", url = "http://user-service:8083")
public interface UserClient {

    @GetMapping("/internal/users/{id}")
    GetUser getUserById(@PathVariable Long id);

    @PostMapping("/internal/users")
    GetUser createUser(@RequestBody CreateUser createUser);

    @PutMapping("/internal/users/last_login/{id}")
    GetUser updateLastLogin(@PathVariable Long id);
}
