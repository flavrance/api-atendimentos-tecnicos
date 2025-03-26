package com.technicalsupport.api.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Model Schema of Error Response")
public class ErrorResponse {
    
    @Schema(description = "Error Date time")
    private LocalDateTime timestamp;
    
    @Schema(description = "Http Status Code")
    private int status;
    
    @Schema(description = "Error Message")
    private String message;
    
    @Schema(description = "Detail list of errors")
    private List<String> errors;
} 