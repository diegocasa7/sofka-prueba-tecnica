package com.sofka.ti.backend.service;

import com.sofka.ti.backend.dto.cliente.*;
import com.sofka.ti.backend.entity.ClienteEntity;
import com.sofka.ti.backend.entity.PersonaEntity;
import com.sofka.ti.backend.exception.NotFoundException;
import com.sofka.ti.backend.repository.ClienteRepository;
import com.sofka.ti.backend.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;

    public ClienteResponse crear(ClienteCreateRequest req) {
        PersonaEntity p = PersonaEntity.builder()
                .nombre(req.persona().nombre())
                .genero(req.persona().genero())
                .edad(req.persona().edad())
                .identificacion(req.persona().identificacion())
                .direccion(req.persona().direccion())
                .telefono(req.persona().telefono())
                .build();

        PersonaEntity personaGuardada = personaRepository.save(p);

        ClienteEntity c = ClienteEntity.builder()
                .persona(personaGuardada)
                .contrasena(req.contrasena())
                .estado(req.estado())
                .build();

        ClienteEntity guardado = clienteRepository.save(c);
        return mapCliente(guardado);
    }

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(this::mapCliente).toList();
    }

    public ClienteResponse obtener(Long id) {
        ClienteEntity c = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
        return mapCliente(c);
    }

    private ClienteResponse mapCliente(ClienteEntity c) {
        PersonaEntity p = c.getPersona();
        PersonaResponse pr = new PersonaResponse(
                p.getPersonaId(),
                p.getNombre(),
                p.getGenero(),
                p.getEdad(),
                p.getIdentificacion(),
                p.getDireccion(),
                p.getTelefono()
        );
        return new ClienteResponse(c.getClienteId(), pr, c.getEstado());
    }
}
