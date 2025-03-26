package com.technicalsupport.application.services.impl;

import com.technicalsupport.application.services.TechnicalSupportService;
import com.technicalsupport.domainmodel.exceptions.TechnicalSupportException;
import com.technicalsupport.domainmodel.valueobjects.DocumentValidator;
import com.technicalsupport.domainmodel.valueobjects.IDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TechnicalSupportServiceImpl implements TechnicalSupportService {

    @Autowired
    private final com.technicalsupport.domainmodel.repositories.TechnicalSupportRepository repository;

    @Override
    @Transactional
    public com.technicalsupport.api.dtos.TechnicalSupportResponseDTO createTechnicalSupport(com.technicalsupport.api.dtos.TechnicalSupportRequestDTO dto) {
        log.info("Creation of technical support by CPF: {}", dto.getCpf());
        try {
            com.technicalsupport.domainmodel.entities.TechnicalSupport technicalSupport = com.technicalsupport.domainmodel.entities.TechnicalSupport.builder()
                .id(IDGenerator.generateRowId(4))
                .cpf(dto.getCpf())
                .complaint(dto.getComplaint())
                .status("A")
                .date(dto.getDate())
                .build();
            log.debug("Technical support ID: {}", technicalSupport.getId());
            if(!technicalSupport.isValid()){
                throw new TechnicalSupportException("CPF is invalid");
            }
            com.technicalsupport.domainmodel.entities.TechnicalSupport saved = repository.save(technicalSupport);
            log.info("Technical support saved. ID: {}", saved.getId());
            return convertToDTO(saved);
        } catch (TechnicalSupportException e) {
            log.error("Error of created technical support by CPF: {}", dto.getCpf(), e);
            throw new com.technicalsupport.domainmodel.exceptions.TechnicalSupportException(e.getMessage(), e);
        }
        catch (Exception e) {
            log.error("Error of created technical support by CPF: {}", dto.getCpf(), e);
            throw new RuntimeException("Error trying create technical support", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.technicalsupport.api.dtos.TechnicalSupportResponseDTO> search(com.technicalsupport.api.dtos.TechnicalSupportSearchCriteriaDTO criteriaDTO) {
        log.info("Searching tecnical supports by CPF: {}", criteriaDTO.getCpf());
        try {

            if(!DocumentValidator.cpfIsValid(criteriaDTO.getCpf())){
                throw new TechnicalSupportException("CPF is invalid");
            }

            List<com.technicalsupport.domainmodel.entities.TechnicalSupport> technicalSupports = repository.findAll(criteriaDTO.getCpf(), criteriaDTO.getDateStart(), criteriaDTO.getDateEnd(), criteriaDTO.getId(), criteriaDTO.getDate(), criteriaDTO.getComplaint(), criteriaDTO.getStatus());
            log.debug("Finded {} technical supports to CPF: {}", technicalSupports.size(), criteriaDTO.getCpf());
            return technicalSupports.stream()
                .map(this::convertToDTO)
                .toList();
        }  catch (TechnicalSupportException e) {
            log.error("Error of find technical supports by CPF: {}", criteriaDTO.getCpf(), e);
            throw new com.technicalsupport.domainmodel.exceptions.TechnicalSupportException(e.getMessage(), e);
        }
        catch (Exception e) {
            log.error("Error to find technical supports by CPF: {}", criteriaDTO.getCpf(), e);
            throw new com.technicalsupport.domainmodel.exceptions.TechnicalSupportException("Error trying find technical supports", e);
        }
    }

    private com.technicalsupport.api.dtos.TechnicalSupportResponseDTO convertToDTO(com.technicalsupport.domainmodel.entities.TechnicalSupport technicalSupport) {
        return com.technicalsupport.api.dtos.TechnicalSupportResponseDTO.builder()
            .id(technicalSupport.getId())
            .cpf(technicalSupport.getCpf())
            .complaint(technicalSupport.getComplaint())
            .status(technicalSupport.getStatus())
            .date(technicalSupport.getDate())
            .build();
    }
}