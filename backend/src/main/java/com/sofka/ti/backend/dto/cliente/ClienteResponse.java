package com.sofka.ti.backend.dto.cliente;

public record ClienteResponse(
        Long clienteId,
        PersonaResponse persona,
        Boolean estado
) {
    
}
