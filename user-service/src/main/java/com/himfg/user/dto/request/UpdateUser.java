package com.himfg.user.dto.request;

import com.himfg.user.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser {

    private Set<Long> roleIds;

    private Boolean isSuperUser;

    private Boolean isStaff;

    private Boolean isActive;

    private Gender gender;

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

    private String maritalStatus;

    private String notes;

    private String src_img;
}
