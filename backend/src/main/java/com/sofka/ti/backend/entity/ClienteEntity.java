package com.sofka.ti.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "cliente")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "persona_id", nullable = false, unique = true)
    private PersonaEntity persona;

    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
