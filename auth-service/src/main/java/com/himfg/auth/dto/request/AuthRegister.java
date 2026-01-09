package com.himfg.auth.dto.request;

import com.himfg.auth.client.user.dto.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class AuthRegister {

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 3, max = 15, message = "La contraseña debe tener al menos 3 caracteres")
    private String password;

    private Set<Long> roleIds;

    private Boolean isSuperUser;

    private Boolean isStaff;

    private Boolean isActive;

    @NotBlank(message = "El nombre es requerido")
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
