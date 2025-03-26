package com.technicalsupport.application.services;

import com.technicalsupport.api.dtos.TechnicalSupportSearchCriteriaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TechnicalSupportServiceTest {

    @Mock
    private com.technicalsupport.domainmodel.repositories.TechnicalSupportRepository repository;

    private TechnicalSupportService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new com.technicalsupport.application.services.impl.TechnicalSupportServiceImpl(repository);
    }

    @Test
    void createTechnicalSupport_MustReturnTechnicalSupportWithStatusA() {
        LocalDateTime date = LocalDateTime.now();
        com.technicalsupport.api.dtos.TechnicalSupportRequestDTO dto = com.technicalsupport.api.dtos.TechnicalSupportRequestDTO.builder()
                .cpf("12345678901")
                .date(date)
                .complaint("Teste")
                .build();

        when(repository.save(any(com.technicalsupport.domainmodel.entities.TechnicalSupport.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));


        com.technicalsupport.api.dtos.TechnicalSupportResponseDTO result = service.createTechnicalSupport(dto);

        // Assert
        assertNotNull(result);
        assertEquals("A", result.getStatus());
        assertEquals(dto.getCpf(), result.getCpf());
        assertEquals(dto.getComplaint(), result.getComplaint());
        verify(repository, times(1)).save(any(com.technicalsupport.domainmodel.entities.TechnicalSupport.class));
    }

    @Test
    void search_MustReturnListOfTechnicalSupports() {

        String cpf = "12345678901";
        LocalDateTime date = LocalDateTime.now();

        TechnicalSupportSearchCriteriaDTO searchCriteriaDTO = TechnicalSupportSearchCriteriaDTO.builder()
                .cpf(cpf)
                .build();

        com.technicalsupport.domainmodel.entities.TechnicalSupport technicalSupport = com.technicalsupport.domainmodel.entities.TechnicalSupport.builder()
                .id(1L)
                .cpf(cpf)
                .complaint("Teste")
                .date(date)
                .status("A")
                .build();

        when(repository.findAll(searchCriteriaDTO.getCpf(),
                searchCriteriaDTO.getDateStart(),
                searchCriteriaDTO.getDateEnd(),
                searchCriteriaDTO.getId(),
                searchCriteriaDTO.getDate(),
                searchCriteriaDTO.getComplaint(),
                searchCriteriaDTO.getStatus())).thenReturn(Arrays.asList(technicalSupport));


        List<com.technicalsupport.api.dtos.TechnicalSupportResponseDTO> resultados = service.search(searchCriteriaDTO);

        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        assertEquals(cpf, resultados.get(0).getCpf());
        verify(repository, times(1)).findAll(searchCriteriaDTO.getCpf(),
                searchCriteriaDTO.getDateStart(),
                searchCriteriaDTO.getDateEnd(),
                searchCriteriaDTO.getId(),
                searchCriteriaDTO.getDate(),
                searchCriteriaDTO.getComplaint(),
                searchCriteriaDTO.getStatus());
    }
} 