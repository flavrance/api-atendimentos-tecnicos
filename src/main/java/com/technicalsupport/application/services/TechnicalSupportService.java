package com.technicalsupport.application.services;

import java.time.LocalDate;
import java.util.List;

public interface TechnicalSupportService {
    com.technicalsupport.api.dtos.TechnicalSupportResponseDTO createTechnicalSupport(com.technicalsupport.api.dtos.TechnicalSupportRequestDTO dto);
    List<com.technicalsupport.api.dtos.TechnicalSupportResponseDTO> search(com.technicalsupport.api.dtos.TechnicalSupportSearchCriteriaDTO criteriaDTO);
} 