package com.technicalsupport.api.controllers;

import com.technicalsupport.api.dtos.TechnicalSupportSearchCriteriaDTO;
import com.technicalsupport.application.services.TechnicalSupportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technicalsupport.domainmodel.entities.TechnicalSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(com.technicalsupport.api.controllers.TechnicalSupportController.class)
class TechnicalSupportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TechnicalSupportService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTechnicalSupport_MustReturnStatus200() throws Exception {

        LocalDateTime date = LocalDateTime.now();
        com.technicalsupport.api.dtos.TechnicalSupportRequestDTO dto = com.technicalsupport.api.dtos.TechnicalSupportRequestDTO.builder()
                .cpf("12345678901")
                .date(date)
                .complaint("Teste")
                .build();

        com.technicalsupport.api.dtos.TechnicalSupportResponseDTO response = com.technicalsupport.api.dtos.TechnicalSupportResponseDTO.builder()
            .id(1L)
            .cpf("12345678901")
            .complaint("Teste")
            .status("A")
            .build();

        when(service.createTechnicalSupport(any(com.technicalsupport.api.dtos.TechnicalSupportRequestDTO.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/technical-supports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.status").value("A"));
    }

    @Test
    void createTechnicalSupport_WithInvalidCPF_MustReturnStatus422() throws Exception {
        LocalDateTime date = LocalDateTime.now();
        com.technicalsupport.api.dtos.TechnicalSupportRequestDTO dto = com.technicalsupport.api.dtos.TechnicalSupportRequestDTO.builder()
                .cpf("123")
                .date(date)
                .complaint("Teste")
                .build();

        mockMvc.perform(post("/api/technical-supports")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void search_MustReturnStatus200() throws Exception {
        String cpf = "12345678901";
        LocalDateTime date = LocalDateTime.now();
        TechnicalSupportSearchCriteriaDTO searchCriteriaDTO = TechnicalSupportSearchCriteriaDTO.builder()
                .cpf(cpf)
                .build();

        com.technicalsupport.api.dtos.TechnicalSupportResponseDTO dto = com.technicalsupport.api.dtos.TechnicalSupportResponseDTO.builder()
                .id(1L)
                .cpf(cpf)
                .complaint("Teste")
                .status("A")
                .date(date)
                .build();

        when(service.search(searchCriteriaDTO))
            .thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/api/technical-supports")
                .header("CPF", cpf))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("1"))
            .andExpect(jsonPath("$[0].cpf").value(cpf));
    }
} 