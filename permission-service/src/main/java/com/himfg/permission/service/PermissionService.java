package com.himfg.permission.service;

import com.himfg.permission.dto.request.CreatePermission;
import com.himfg.permission.dto.request.UpdatePermission;
import com.himfg.permission.entity.Permission;
import com.himfg.permission.exceptions.DuplicateEntryException;
import com.himfg.permission.exceptions.GeneralServiceException;
import com.himfg.permission.exceptions.NoDataFoundException;
import com.himfg.permission.repository.PermissionRepository;
import com.himfg.permission.service.dto.GetPermission;
import org.apache.commons.collections4.Get;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public List<GetPermission> getAllPermissions() {

        List<Permission> permissions = permissionRepository.findAll();

        return permissions.stream().map((permission) ->
            GetPermission.builder()
                    .permissionId(permission.getPermissionId())
                    .name(permission.getName())
                    .codename(permission.getCodename())
                    .description(permission.getDescription())
                    .application(permission.getApplication())
                    .build()
        ).toList();
    }

    public Permission getInternalPermissionById(Long id) {

        return permissionRepository.findById(id).orElseThrow(() -> new NoDataFoundException("No existe permiso con ese id"));
    }

    public GetPermission getPermissionById(Long id) {

        Permission permission = permissionRepository.findById(id).orElseThrow(() -> new NoDataFoundException("No existe permiso con ese id"));

        return  GetPermission.builder()
                .permissionId(permission.getPermissionId())
                .name(permission.getName())
                .codename(permission.getCodename())
                .description(permission.getDescription())
                .application(permission.getApplication())
                .build();
    }

    public List<GetPermission> getPermissionByApplication(String application) {

        List<Permission> permissions = permissionRepository.findByApplication(application);

        if(permissions.isEmpty()){
            throw new NoDataFoundException("No existe permiso con ese aplicacion");
        }

        return permissions.stream().map((permission) ->
                GetPermission.builder()
                        .permissionId(permission.getPermissionId())
                        .name(permission.getName())
                        .codename(permission.getCodename())
                        .description(permission.getDescription())
                        .application(permission.getApplication())
                        .build()
                ).toList();
    }

    public GetPermission createPermission(CreatePermission request) {
        try {
            Permission permission = permissionRepository.save(Permission.builder()
                    .name(request.getName())
                    .codename(request.getCodename())
                    .description(request.getDescription())
                    .build());

            return GetPermission.builder()
                    .permissionId(permission.getPermissionId())
                    .name(permission.getName())
                    .codename(permission.getCodename())
                    .description(permission.getDescription())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException("Un permiso ya existe con ese codename.", e);
        }
    }

    public GetPermission updatePermission(Long id, UpdatePermission permissionDetails) {
        try {

            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe permiso con ese id"));

            permission.setName(permissionDetails.getName());
            permission.setDescription(permissionDetails.getDescription());
            permission.setCodename(permissionDetails.getCodename());

            Permission updatedPermission = permissionRepository.save(permission);

            return GetPermission.builder()
                    .permissionId(updatedPermission.getPermissionId())
                    .name(updatedPermission.getName())
                    .codename(updatedPermission.getCodename())
                    .description(updatedPermission.getDescription())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException("Un permiso ya existe con ese codename.", e);
        }
    }

    public void deletePermission(Long id) {

            Permission permission = permissionRepository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("No existe permiso con ese id"));

            permissionRepository.delete(permission);
    }

    public void importExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheet("Permisos");
            if (sheet == null) {
                throw new RuntimeException("La hoja 'Permisos' no existe en el Excel");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // Saltamos la fila 0 de headers
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Permission permission = new Permission();
                permission.setCodename(row.getCell(0).getStringCellValue());
                permission.setName(row.getCell(1).getStringCellValue());
                permission.setDescription(row.getCell(2).getStringCellValue());

                permissionRepository.save(permission);
            }

        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

}
