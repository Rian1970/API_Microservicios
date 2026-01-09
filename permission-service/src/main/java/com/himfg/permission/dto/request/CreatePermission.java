package com.himfg.permission.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePermission {

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 3, max = 15, message = "El nombre debe tener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "El codename es requerido")
    @Size(min = 3, max = 15, message = "El codename debe tener al menos 3 caracteres")
    private String codename;

    @NotBlank(message = "La descipción es requerida")
    @Size(min = 3, max = 15, message = "La descripción debe tener al menos 3 caracteres")
    private String description;
}
