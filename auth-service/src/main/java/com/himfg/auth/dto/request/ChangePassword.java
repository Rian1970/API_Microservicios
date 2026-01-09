package com.himfg.auth.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePassword {

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 3, max = 15, message = "La contraseña debe tener al menos 3 caracteres")
    private String password;

    @NotBlank(message = "La confirmación de contraseña es requerida")
    @Size(min = 3, max = 15, message = "La contraseña debe tener al menos 3 caracteres")
    private String confirmPassword;

    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordMatching() {
        return password.equals(confirmPassword);
    }
}
