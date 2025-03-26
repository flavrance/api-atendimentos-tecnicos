package com.technicalsupport.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalSupportResponseDTO {
    private Long id;

    @NotBlank(message = "CPF is required")
    @Pattern(regexp = "\\d{11}", message = "CPF must contain 11 numeric digits")
    private String cpf;

    @NotBlank(message = "Complaint is required")
    private String complaint;

    private String status;

    private LocalDateTime date;
} 