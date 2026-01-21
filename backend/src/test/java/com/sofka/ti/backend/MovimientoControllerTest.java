package com.sofka.ti.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.ti.backend.controller.MovimientoController;
import com.sofka.ti.backend.service.MovimientoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MovimientoController.class)
class MovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovimientoService movimientoService;

    @Test
    void debeRetornarSaldoNoDisponibleCuandoIntentanRetiroSinSaldo() throws Exception {
    when(movimientoService.crear(any()))
            .thenThrow(new com.sofka.ti.backend.exception.BusinessException("Saldo no disponible"));


        var body = """
        {
          "tipoMovimiento": "RETIRO",
          "valor": 100.0,
          "cuentaId": 1,
          "numeroCuenta": "478758",
          "fecha": "2026-01-20"
        }
        """;

        mockMvc.perform(
                post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Saldo no disponible")));


    }
}
