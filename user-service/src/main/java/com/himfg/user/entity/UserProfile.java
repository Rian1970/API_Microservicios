package com.himfg.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private Long userId;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private Boolean emailVerified;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime emailVerifiedAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime lastLogin;

    private String lastLoginIp;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    private Set<Long> roleIds;

    @NotNull
    private Boolean isSuperUser;

    private Boolean isStaff;

    private Boolean isActive;

    @NotNull
    private String name;

    private String middleName;

    private String firstLastName;

    private String secondLastName;

    private LocalDate birthdate;

    @NotNull
    @Enumerated(EnumType.STRING)
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

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime dateJoined;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime updatedAt;

    private Boolean isDeleted;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime deletedAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime restoredAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime blockedAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private LocalDateTime unblockedAt;

    private Long blockedBy;

    @NotNull
    private Long createdBy;

    private Long deletedBy;

    private Long restoredBy;

    private Long unblockedBy;

    private Long updatedBy;
}
