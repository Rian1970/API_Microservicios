package com.himfg.role.controller;

import com.himfg.role.dto.request.AssignPermissions;
import com.himfg.role.dto.request.CreateRole;
import com.himfg.role.dto.request.UpdateRole;
import com.himfg.role.dto.response.Base.BaseResponse;
import com.himfg.role.dto.response.Base.ResponseBuilder;
import com.himfg.role.entity.Role;
import com.himfg.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllRoles() {
        return ResponseBuilder.ok("Roles obtenidos", roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getRoleById(@PathVariable Long id) {
        return ResponseBuilder.ok("Rol obtenido", roleService.getRoleById(id));
    }

    @GetMapping("/aplicacion/{application}")
    public ResponseEntity<BaseResponse> getRoleByAplicationId(@PathVariable String application) {
        return ResponseBuilder.ok("Roles encontrados", roleService.getRolesByApplication(application));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createRole(@RequestBody CreateRole request) {
        return ResponseBuilder.ok("Rol creado exitosamente", roleService.createRole(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateRole(@PathVariable Long id, @RequestBody UpdateRole roleDetails) {
        return ResponseBuilder.ok("Rol editado exitosamente", roleService.updateRole(id, roleDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseBuilder.ok("Rol eliminado exitosamente", null);
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<BaseResponse> assignPermissions(@PathVariable Long id, @RequestBody AssignPermissions request) {
        return ResponseBuilder.ok("Permiso asignado al rol", roleService.assignPermissions(id, request.getPermissionIds()));
    }

    @PostMapping("/upload_from_excel")
    public ResponseEntity<BaseResponse> uploadPermissions(@RequestParam("file") MultipartFile file) {
        roleService.importExcel(file);
        return ResponseBuilder.ok("Permisos importados correctamente",  null);
    }
}
