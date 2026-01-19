package com.sofka.ti.backend.repository;

import com.sofka.ti.backend.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {
    
}
