package com.himfg.user.controller;

import com.himfg.user.dto.request.AssignRoles;
import com.himfg.user.dto.request.CreateUser;
import com.himfg.user.dto.request.UpdateUser;
import com.himfg.user.dto.response.Base.BaseResponse;
import com.himfg.user.dto.response.Base.ResponseBuilder;
import com.himfg.user.service.UserService;
import com.himfg.user.dto.response.GetUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllUsers() {
        return ResponseBuilder.ok("Usuarios obtenidos", userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getUserById(@PathVariable Long id) {
        return ResponseBuilder.ok("Usuario obtenido.", userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateUser(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @RequestBody UpdateUser userDetails) {
        Long id_user_modifier = jwt.getClaim("userId");
        return ResponseBuilder.ok("Usuario actualizado", userService.updateUser(id_user_modifier, id, userDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseBuilder.ok("Usuario eliminado", null);
    }

    @DeleteMapping("/soft/{id}")
    public ResponseEntity<BaseResponse> softDeleteUser(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        Long id_user_modifier = jwt.getClaim("userId");
        userService.softDeleteUser(id_user_modifier, id);
        return ResponseBuilder.ok("Usuario eliminado", null);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<BaseResponse> assignRoles(@PathVariable Long id, @RequestBody AssignRoles request) {
        return ResponseBuilder.ok("Rol asignado al usuario", userService.assignRoles(id, request.getRoleIds()));
    }
}
