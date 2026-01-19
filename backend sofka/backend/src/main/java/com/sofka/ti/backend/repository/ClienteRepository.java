package com.sofka.ti.backend.repository;

import com.sofka.ti.backend.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    
}
