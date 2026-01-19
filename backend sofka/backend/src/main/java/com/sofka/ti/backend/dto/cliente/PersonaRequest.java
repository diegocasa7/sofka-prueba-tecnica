package com.sofka.ti.backend.dto.cliente;

import jakarta.validation.constraints.*;

public record PersonaRequest(
        @NotBlank String nombre,
        String genero,
        @Min(0) Integer edad,
        @NotBlank String identificacion,
        String direccion,
        String telefono
) {
    
}
