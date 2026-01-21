package com.sofka.ti.backend.dto.cliente;

public record PersonaResponse(
        Long personaId,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono
) {
    
}
