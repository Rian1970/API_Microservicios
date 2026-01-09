package com.himfg.user.dto.response;

import com.himfg.user.entity.Gender;
import com.himfg.user.service.dto.Permission;
import com.himfg.user.service.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUser {
    private Long userId;

    private String email;

    private Boolean emailVerified;

    private LocalDateTime emailVerifiedAt;

    private LocalDateTime lastLogin;

    private String lastLoginIp;

    private Set<Role> roles;

    private Set<Permission> permissions;

    private Boolean isSuperUser;

    private Boolean isStaff;

    private Boolean isActive;

    private String name;

    private String middleName;

    private String firstLastName;

    private String secondLastName;

    private LocalDate birthdate;

    private Gender gender;

    private String curp;

    private String phoneNumber;

    private String cellphone;

    private Integer addressCatNacionalidadId;

    private Integer addressCatEntidadId;

    private String addressCity;

    private String addressZipCode;

    private String addressNeighborhood;

    private String address;

    private String addressNumber;

    private String addressInteriorNumber;

    private String addressComplement;

    private String jobPosition;

    private String employeeNumber;

    private String license;

    private String rfc;

    private String maritalStatus;

    private String notes;

    private String src_img;
}
