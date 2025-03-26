package com.technicalsupport.infrastructure.repositories;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.Expression;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DynamoDBTechnicalSupportRepository implements com.technicalsupport.domainmodel.repositories.TechnicalSupportRepository {

    private final DynamoDbTable<com.technicalsupport.domainmodel.entities.TechnicalSupport> table;

    public DynamoDBTechnicalSupportRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table = dynamoDbEnhancedClient.table("TechnicalSupports",
            TableSchema.fromBean(com.technicalsupport.domainmodel.entities.TechnicalSupport.class));
    }

    @Override
    public com.technicalsupport.domainmodel.entities.TechnicalSupport save(com.technicalsupport.domainmodel.entities.TechnicalSupport technicalSupport) {
        table.putItem(technicalSupport);
        return technicalSupport;
    }

    @Override
    public List<com.technicalsupport.domainmodel.entities.TechnicalSupport> findAll(
            String cpf,
            Optional<LocalDate> dateStart,
            Optional<LocalDate> dateEnd,
            Optional<Long> id,
            Optional<LocalDateTime> date,
            Optional<String> complaint,
            Optional<String> status) {

        LocalDate start = dateStart.orElse(null);
        LocalDate end = dateEnd.orElse(null);
        Long complaintId = id.orElse(null);
        LocalDateTime complaintDate = date.orElse(null);
        String complaintText = complaint.orElse(null);
        String complaintStatus = status.orElse(null);

        return findAll(cpf, start, end, complaintId, complaintDate, complaintText, complaintStatus);
    }

    @Override
    public List<com.technicalsupport.domainmodel.entities.TechnicalSupport> findAll(String cpf, LocalDate dateStart, LocalDate dateEnd, Long id, LocalDateTime date, String complaint, String status) {
        QueryConditional queryConditional = QueryConditional
            .keyEqualTo(Key.builder().partitionValue(cpf).build());

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional);


        Map<String, AttributeValue> expressionValues = new HashMap<>();
        StringBuilder filterExpression = new StringBuilder();

        if (dateStart != null && dateEnd != null) {
            filterExpression.append(" #date BETWEEN :dateStart AND :dateEnd");
            LocalDateTime start = LocalDateTime.of(dateStart, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(dateEnd, LocalTime.MAX);
            expressionValues.put(":dateStart", AttributeValue.builder().s(start.toString()).build());
            expressionValues.put(":dateEnd", AttributeValue.builder().s(end.toString()).build());
        }

        if (id != null) {
            appendAnd(filterExpression);
            filterExpression.append(" #id = :id");
            expressionValues.put(":id", AttributeValue.builder().n(id.toString()).build());
        }

        if (date != null) {
            appendAnd(filterExpression);
            filterExpression.append(" #date = :date");
            expressionValues.put(":date", AttributeValue.builder().s(date.toString()).build());
        }

        if (complaint != null && !complaint.isEmpty()) {
            appendAnd(filterExpression);
            filterExpression.append(" contains(#complaint, :complaint)");
            expressionValues.put(":complaint", AttributeValue.builder().s(complaint).build());
        }

        if (status != null && !status.isEmpty()) {
            appendAnd(filterExpression);
            filterExpression.append(" #status = :status");
            expressionValues.put(":status", AttributeValue.builder().s(status).build());
        }

        Map<String, String> expressionNames = new HashMap<>();

        if (dateStart != null && dateEnd != null || date != null) {
            expressionNames.put("#date", "date");
        }
        if (id != null) {
            expressionNames.put("#id", "id");
        }
        if (complaint != null && !complaint.isEmpty()) {
            expressionNames.put("#complaint", "complaint");
        }
        if (status != null && !status.isEmpty()) {
            expressionNames.put("#status", "status");
        }

        if (!filterExpression.isEmpty()) {
            requestBuilder.filterExpression(Expression.builder()
                    .expression(filterExpression.toString())
                    .expressionValues(expressionValues)
                    .expressionNames(expressionNames)
                    .build());
        }


        return table.query(requestBuilder.build())
            .items()
            .stream()
            .collect(Collectors.toList());
    }

    private void appendAnd(StringBuilder filterExpression) {
        if (!filterExpression.isEmpty()) {
            filterExpression.append(" AND ");
        }
    }
} 