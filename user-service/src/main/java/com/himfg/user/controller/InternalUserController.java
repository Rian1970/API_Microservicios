package com.himfg.user.controller;

import com.himfg.user.dto.request.CreateUser;
import com.himfg.user.dto.request.UpdateUser;
import com.himfg.user.dto.response.Base.BaseResponse;
import com.himfg.user.dto.response.Base.ResponseBuilder;
import com.himfg.user.dto.response.GetUser;
import com.himfg.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public GetUser getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public GetUser createUser(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateUser request) {
        Long id_user_create = jwt.getClaim("userId");
        return userService.createUser(id_user_create, request);
    }

    @PutMapping("/last_login/{id}")
    public GetUser updateLastLogin(@PathVariable Long id) {
        return userService.updateLastLogin(id);
    }
}
