package com.sofka.ti.backend.dto.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record ClienteCreateRequest(
    @Valid @NotNull PersonaRequest persona,
        @NotBlank String contrasena,
        @NotNull Boolean estado
) {
    
}
