package com.technicalsupport.domainmodel.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TechnicalSupportRepository {
    com.technicalsupport.domainmodel.entities.TechnicalSupport save(com.technicalsupport.domainmodel.entities.TechnicalSupport technicalSupport);
    List<com.technicalsupport.domainmodel.entities.TechnicalSupport> findAll(
            String cpf,
            Optional<LocalDate> dateStart,
            Optional<LocalDate> dateEnd,
            Optional<Long> id,
            Optional<LocalDateTime> date,
            Optional<String> complaint,
            Optional<String> status);
    List<com.technicalsupport.domainmodel.entities.TechnicalSupport> findAll(String cpf, LocalDate dateStart, LocalDate dateEnd, Long id, LocalDateTime date, String complaint, String status);
} 