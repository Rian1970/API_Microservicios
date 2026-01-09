package com.himfg.permission.controller;

import com.himfg.permission.dto.request.CreatePermission;
import com.himfg.permission.dto.request.UpdatePermission;
import com.himfg.permission.dto.response.Base.BaseResponse;
import com.himfg.permission.dto.response.Base.ResponseBuilder;
import com.himfg.permission.entity.Permission;
import com.himfg.permission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllPermissions() {
        return ResponseBuilder.ok("Permisos obtenidos", permissionService.getAllPermissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getPermissionById(@PathVariable Long id) {
        return ResponseBuilder.ok("Permiso obtenido", permissionService.getPermissionById(id));
    }

    @GetMapping("/aplicacion/{application}")
    public ResponseEntity<BaseResponse> getPermissionByAplicacion(@PathVariable String application){
        return ResponseBuilder.ok("Permisos obtenidos", permissionService.getPermissionByApplication(application));
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createPermission(@RequestBody CreatePermission request) {
        return ResponseBuilder.ok("Permiso creado", request, permissionService.createPermission(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePermission(@PathVariable Long id, @RequestBody UpdatePermission permissionDetails) {
            return ResponseBuilder.ok("Permiso editado", permissionDetails, permissionService.updatePermission(id, permissionDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);

        return ResponseBuilder.ok("Permiso eliminado", null);
    }

    @PostMapping("/upload_from_excel")
    public ResponseEntity<BaseResponse> uploadPermissions(@RequestParam("file") MultipartFile file) {
        permissionService.importExcel(file);

        return ResponseBuilder.ok("Permisos importados correctamente", null);
    }
}
