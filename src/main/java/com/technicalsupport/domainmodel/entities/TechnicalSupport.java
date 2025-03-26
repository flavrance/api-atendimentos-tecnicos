package com.technicalsupport.domainmodel.entities;

import com.technicalsupport.domainmodel.exceptions.TechnicalSupportException;
import com.technicalsupport.domainmodel.valueobjects.DocumentValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class TechnicalSupport {
    private static final Set<String> VALID_STATUS = Set.of("A"); // A = Aberto
    
    private Long id;
    private String cpf;
    private String complaint;
    private String status;
    private LocalDateTime date;

    @DynamoDbPartitionKey
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF must contain 11 numeric digits");
        }

        this.cpf = cpf;
    }

    @DynamoDbSortKey
    public Long getId() {
        return id;
    }

    public void setStatus(String status) {
        if (status == null || !VALID_STATUS.contains(status)) {
            throw new IllegalArgumentException("Initial status must bd 'A' (Open)");
        }
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date isn't null");
        }
        this.date = date;
    }

    public void setComplaint(String complaint) {
        if (complaint == null || complaint.trim().isEmpty()) {
            throw new IllegalArgumentException("Complaint cannot be null or empty");
        }
        if (complaint.length() > 500) {
            throw new IllegalArgumentException("Complaint cannot be longer than 500 characters");
        }
        this.complaint = complaint.trim();
    }

    public Boolean isValid(){
        return DocumentValidator.cpfIsValid(this.getCpf());
    }
} 