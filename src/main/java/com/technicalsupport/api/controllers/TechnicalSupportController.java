package com.technicalsupport.api.controllers;

import com.technicalsupport.api.dtos.TechnicalSupportSearchCriteriaDTO;
import com.technicalsupport.api.models.ErrorResponse;
import com.technicalsupport.application.services.TechnicalSupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/technical-supports")
@RequiredArgsConstructor
@Validated
@Tag(name = "Atendimentos Técnicos", description = "API para gerenciamento de atendimentos técnicos")
public class TechnicalSupportController {

    @Autowired
    private final TechnicalSupportService service;

    @PostMapping
    @Operation(summary = "Criar novo atendimento técnico", 
              description = "Cria um novo atendimento técnico com status inicial 'A' (Aberto)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Atendimento criado com sucesso"),
        @ApiResponse(responseCode = "422", description = "Dados inválidos fornecidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<com.technicalsupport.api.dtos.TechnicalSupportResponseDTO> createThecnicalSupport(
            @Valid @RequestBody com.technicalsupport.api.dtos.TechnicalSupportRequestDTO dto) {
        return ResponseEntity.ok(service.createTechnicalSupport(dto));
    }

    @GetMapping
    @Operation(summary = "Buscar atendimentos ",
              description = "Retorna todos os atendimentos técnicos do cliente identificado pelo CPF")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Header CPF não fornecido ou inválido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Nenhum atendimento encontrado para o CPF informado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<com.technicalsupport.api.dtos.TechnicalSupportResponseDTO>> search(
            @RequestHeader("CPF") 
            @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
            @Parameter(description = "CPF do cliente (11 dígitos numéricos)", required = true) 
            String cpf,
            @RequestParam(name = "dateStart", required = false)
            @Parameter(description = "Data inicial para pesquisa", required = false)
            LocalDate dateStart,
            @RequestParam(name = "dateEnd", required = false)
            @Parameter(description = "Data final para pesquisa", required = false)
            LocalDate dateEnd,
            @RequestParam(name = "id", required = false)
            @Parameter(description = "Id para pesquisa", required = false)
            Long id,
            @RequestParam(name = "date", required = false)
            @Parameter(description = "Data de criação para pesquisa", required = false)
            LocalDateTime date,
            @RequestParam(name = "complaint", required = false)
            @Parameter(description = "Reclamação do cliente para pesquisa", required = false)
            String complaint,
            @RequestParam(name = "status", required = false)
            @Parameter(description = "Status dos atendimentos para pesquisa", required = false)
            String status) {

        TechnicalSupportSearchCriteriaDTO searchCriteriaDTO = TechnicalSupportSearchCriteriaDTO.builder()
                .id(Optional.ofNullable(id))
                .complaint(Optional.ofNullable(complaint))
                .cpf(cpf)
                .dateEnd(Optional.ofNullable(dateEnd))
                .dateStart(Optional.ofNullable(dateStart))
                .date(Optional.ofNullable(date))
                .status(Optional.ofNullable(status))
                .build();

        List<com.technicalsupport.api.dtos.TechnicalSupportResponseDTO> technicalSupports = service.search(searchCriteriaDTO);
//        if (technicalSupports.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(technicalSupports);
    }
} 