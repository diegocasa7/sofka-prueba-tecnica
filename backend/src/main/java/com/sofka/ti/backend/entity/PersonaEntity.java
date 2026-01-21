package com.sofka.ti.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "persona")
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long personaId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "identificacion", nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(name = "direccion", length = 150)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;
}
