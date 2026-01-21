package com.sofka.ti.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.ti.backend.controller.ClienteController;
import com.sofka.ti.backend.dto.cliente.ClienteResponse;
import com.sofka.ti.backend.dto.cliente.PersonaResponse;
import com.sofka.ti.backend.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    void debeCrearCliente() throws Exception {

        PersonaResponse persona = new PersonaResponse(
                1L,
                "Jose Lema",
                "M",
                30,
                "1234567890",
                "Otavalo sn y principal",
                "098254785"
        );

        ClienteResponse response = new ClienteResponse(
                1L,
                persona,
                true
        );

        when(clienteService.crear(any())).thenReturn(response);

        var body = """
        {
        "persona": {
            "nombre": "Jose Lema",
            "genero": "M",
            "edad": 30,
            "identificacion": "1234567890",
            "direccion": "Otavalo sn y principal",
            "telefono": "098254785"
        },
        "contrasena": "1234",
        "estado": true
        }
        """;


        mockMvc.perform(
                post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().is2xxSuccessful());
    }
}
