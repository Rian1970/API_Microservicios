package com.himfg.role.service;

import com.himfg.role.client.permission.PermissionClient;
import com.himfg.role.dto.request.CreateRole;
import com.himfg.role.dto.request.UpdateRole;
import com.himfg.role.entity.Role;
import com.himfg.role.exceptions.DuplicateEntryException;
import com.himfg.role.exceptions.GeneralServiceException;
import com.himfg.role.exceptions.NoDataFoundException;
import com.himfg.role.repository.RoleRepository;
import com.himfg.role.service.dto.GetRole;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private  RoleRepository roleRepository;

    @Autowired
    private PermissionClient permissionClient;

    public List<GetRole> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        return roles.stream().map((rol) ->
                GetRole.builder()
                        .roleId(rol.getRoleId())
                        .name(rol.getName())
                        .codename(rol.getCodename())
                        .description(rol.getDescription())
                        .notes(rol.getNotes())
                        .permissionIds(rol.getPermissionIds())
                        .build()
        ).toList();
    }

    public GetRole getInternalRoleById(Long id) {

        Role rol = roleRepository.findById(id).orElseThrow(() -> new NoDataFoundException("Permission not found with id: " + id));

        return GetRole.builder()
                .roleId(rol.getRoleId())
                .name(rol.getName())
                .codename(rol.getCodename())
                .description(rol.getDescription())
                .notes(rol.getNotes())
                .permissionIds(rol.getPermissionIds())
                .build();
    }

    public Role getRoleById(Long id) {

        return roleRepository.findById(id).orElseThrow(() -> new NoDataFoundException("Permission not found with id: " + id));
    }

    public List<GetRole> getRolesByApplication(String application) {
        List<Role> roles = roleRepository.findByApplication(application);

        if(roles.isEmpty()){
            throw new NoDataFoundException("No hay roles para esa aplicación");
        }

        return roles.stream().map((role) ->
                GetRole.builder()
                        .roleId(role.getRoleId())
                        .name(role.getName())
                        .codename(role.getCodename())
                        .description(role.getDescription())
                        .notes(role.getNotes())
                        .permissionIds(role.getPermissionIds())
                        .application(role.getApplication())
                        .build()
                ).toList();
    }

    public GetRole createRole(CreateRole request) {
        try {

            Role role = roleRepository.save(Role.builder()
                    .name(request.getName())
                    .codename(request.getCodename())
                    .description(request.getDescription())
                    .notes(request.getNotes())
                    .permissionIds(request.getPermissionIds())
                    .build());

            return GetRole.builder()
                    .roleId(role.getRoleId())
                    .name(role.getName())
                    .codename(role.getCodename())
                    .description(role.getDescription())
                    .notes(role.getNotes())
                    .permissionIds(role.getPermissionIds())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException("Un rol ya existe con ese codename.", e);
        }
    }

    public GetRole updateRole(Long id, UpdateRole roleDetails) {
        try{
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new NoDataFoundException("Role not found with id: " + id));

            role.setName(roleDetails.getName());
            role.setDescription(roleDetails.getDescription());
            role.setCodename(roleDetails.getCodename());
            role.setPermissionIds(roleDetails.getPermissionIds());

            Role updatedRole = roleRepository.save(role);

            return GetRole.builder()
                    .roleId(updatedRole.getRoleId())
                    .name(updatedRole.getName())
                    .codename(updatedRole.getCodename())
                    .description(updatedRole.getDescription())
                    .permissionIds(updatedRole.getPermissionIds())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException("Un rol ya existe con ese codename.", e);
        }

    }

    public void deleteRole(Long id) {

        Role role = roleRepository.findById(id).orElseThrow(() -> new NoDataFoundException("Role not found with id: " + id));

        roleRepository.delete(role);

    }

    public GetRole assignPermissions(Long roleId, Set<Long> permissionIds) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoDataFoundException("Role not found with id: " + roleId));

        // Validate permissions by calling permission-service
        permissionIds.forEach(permissionId -> {
            try {
                permissionClient.getInternalPermissionById(permissionId);
            } catch (Exception e) {
                throw new NoDataFoundException("Invalid permissionId: " + permissionId);
            }
        });

        role.setPermissionIds(permissionIds);

        Role updatedRole = roleRepository.save(role);

        return GetRole.builder()
                .roleId(updatedRole.getRoleId())
                .name(updatedRole.getName())
                .codename(updatedRole.getCodename())
                .description(updatedRole.getDescription())
                .notes(updatedRole.getNotes())
                .permissionIds(updatedRole.getPermissionIds())
                .build();
    }

    public void importExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheet("Roles");
            if (sheet == null) {
                throw new RuntimeException("La hoja 'Roles' no existe en el Excel");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {  // Saltamos la fila 0 de headers
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Role role = new Role();
                role.setCodename(row.getCell(0).getStringCellValue());
                role.setName(row.getCell(1).getStringCellValue());
                role.setDescription(row.getCell(2).getStringCellValue());
                role.setNotes(row.getCell(3).getStringCellValue());

                roleRepository.save(role);
            }

        } catch (Exception e) {
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }
}
