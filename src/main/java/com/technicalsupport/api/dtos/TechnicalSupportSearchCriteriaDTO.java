package com.technicalsupport.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalSupportSearchCriteriaDTO {
    private String cpf;
    private Optional<LocalDate> dateStart = null;
    private Optional<LocalDate> dateEnd = null;
    private Optional<Long> id = null;
    private Optional<LocalDateTime> date = null;
    private Optional<String> complaint = null;
    private Optional<String> status = null;
}
