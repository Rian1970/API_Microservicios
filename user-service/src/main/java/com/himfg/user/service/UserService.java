package com.himfg.user.service;

import com.himfg.user.client.Permission.PermissionClient;
import com.himfg.user.client.Permission.dto.GetPermission;
import com.himfg.user.client.Role.RoleClient;
import com.himfg.user.client.Role.dto.GetRole;
import com.himfg.user.dto.request.CreateUser;
import com.himfg.user.dto.request.UpdateUser;
import com.himfg.user.dto.response.GetUser;
import com.himfg.user.service.dto.Permission;
import com.himfg.user.service.dto.Role;
import com.himfg.user.entity.UserProfile;
import com.himfg.user.exceptions.NoDataFoundException;
import com.himfg.user.repository.UserProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserProfileRepository userRepository;

    @Autowired
    private RoleClient roleClient;

    @Autowired
    private PermissionClient permissionClient;

    @Autowired
    private ModelMapper modelMapper;

    public List<GetUser> getAllUsers() {

        List<UserProfile> userProfiles = userRepository.findAll();

        return userProfiles.stream().map(this::putRolesAndPermissions).toList();
    }

    public GetUser getUserById(Long id) {

        UserProfile userProfile = userRepository.findById(id).orElseThrow(() -> new NoDataFoundException("Permission not found with id: " + id));

        return this.putRolesAndPermissions(userProfile);
    }

    public GetUser createUser(Long id_user_create, CreateUser request) {

        UserProfile userProfile = modelMapper.map(request, UserProfile.class);

        userProfile.setEmailVerified(true);
        userProfile.setEmailVerifiedAt(LocalDateTime.now());
        userProfile.setLastLogin(null);
        userProfile.setLastLoginIp(null);
        userProfile.setIsDeleted(false);
        userProfile.setDeletedAt(null);
        userProfile.setRestoredAt(null);
        userProfile.setBlockedAt(null);
        userProfile.setUnblockedAt(null);
        userProfile.setBlockedBy(null);
        userProfile.setCreatedBy(id_user_create);
        userProfile.setDeletedBy(null);
        userProfile.setRestoredBy(null);
        userProfile.setUnblockedBy(null);
        userProfile.setUpdatedBy(null);

        UserProfile newUser = userRepository.save(userProfile);

        return this.putRolesAndPermissions(newUser);

    }

    public GetUser updateUser(Long id_user_modifier, Long id_user, UpdateUser userDetails) {

        UserProfile user = userRepository.findById(id_user)
                .orElseThrow(() -> new NoDataFoundException("User not found with id: " + id_user));

        user.setRoleIds(userDetails.getRoleIds());
        user.setIsSuperUser(userDetails.getIsSuperUser());
        user.setIsStaff(userDetails.getIsStaff());
        user.setIsActive(userDetails.getIsActive());
        user.setGender(userDetails.getGender());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setCellphone(userDetails.getCellphone());
        user.setAddressCatNacionalidadId(userDetails.getAddressCatNacionalidadId());
        user.setAddressCatEntidadId(userDetails.getAddressCatEntidadId());
        user.setAddressCity(userDetails.getAddressCity());
        user.setAddressZipCode(userDetails.getAddressZipCode());
        user.setAddressNeighborhood(userDetails.getAddressNeighborhood());
        user.setAddress(userDetails.getAddress());
        user.setAddressNumber(userDetails.getAddressNumber());
        user.setAddressInteriorNumber(userDetails.getAddressInteriorNumber());
        user.setAddressComplement(userDetails.getAddressComplement());
        user.setJobPosition(userDetails.getJobPosition());
        user.setEmployeeNumber(userDetails.getEmployeeNumber());
        user.setLicense(userDetails.getLicense());
        user.setMaritalStatus(userDetails.getMaritalStatus());
        user.setNotes(userDetails.getNotes());
        user.setSrc_img(userDetails.getSrc_img());
        user.setUpdatedBy(id_user_modifier);

        UserProfile updatedUser = userRepository.save(user);

        return this.putRolesAndPermissions(updatedUser);
    }

    public GetUser updateLastLogin(Long userId){

        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("User not found with id: " + userId));

        user.setLastLogin(LocalDateTime.now());

        UserProfile updatedUser = userRepository.save(user);

        return this.putRolesAndPermissions(updatedUser);

    }

    public void deleteUser(Long id) {

        UserProfile user = userRepository.findById(id).orElseThrow(() -> new NoDataFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public void softDeleteUser(Long id_user_modifier, Long id) {
        UserProfile user = userRepository.findById(id).orElseThrow(() -> new NoDataFoundException("User not found with id: " + id));

        user.setDeletedBy(id_user_modifier);
        user.setIsDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setIsActive(false);

        userRepository.save(user);
    }

    public GetUser assignRoles(Long userId, Set<Long> roleIds) {

        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new NoDataFoundException("User not found with id: " + userId));

        // Validate roles by calling role-service
        roleIds.forEach(roleId -> {
            try {
                roleClient.getInternalRoleById(roleId);
            } catch (Exception e) {
                throw new RuntimeException("Invalid roleId: " + roleId);
            }
        });

        user.setRoleIds(roleIds);

        UserProfile updateUser = userRepository.save(user);

        return this.putRolesAndPermissions(updateUser);
    }

    private GetUser putRolesAndPermissions(UserProfile user){
        GetUser userComplete = modelMapper.map(user, GetUser.class);

        Set<Long> roleIds = user.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roleSummaries = new HashSet<>();

            for (Long roleId : roleIds) {
                GetRole role = roleClient.getInternalRoleById(roleId);

                if (role == null) {
                    continue;
                }

                Role roleSummary = new Role();
                roleSummary.setRoleId(role.getRoleId());
                roleSummary.setName(role.getName());
                roleSummary.setCodename(role.getCodename());

                Set<Long> permissionIds = role.getPermissionIds();
                if (permissionIds != null && !permissionIds.isEmpty()) {
                    Set<Permission> permissionSummaries = permissionIds.stream()
                            .map(permissionId -> {
                                GetPermission perm = permissionClient.getInternalPermissionById(permissionId);

                                if (perm == null) {
                                    return null;
                                }

                                Permission ps = new Permission();
                                ps.setPermissionId(perm.getPermissionId());
                                ps.setName(perm.getName());
                                ps.setCodename(perm.getCodename());
                                return ps;
                            })
                            .filter(p -> p != null)
                            .collect(Collectors.toSet());

                    userComplete.setPermissions(permissionSummaries);
                }

                roleSummaries.add(roleSummary);
            }

            userComplete.setRoles(roleSummaries);
        }

        return userComplete;
    }
}
