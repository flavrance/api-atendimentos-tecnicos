#!/bin/bash

echo "Aguardando DynamoDB Local iniciar..."
sleep 5

echo "Criando tabela AtendimentosTecnicos..."
aws dynamodb create-table \
    --table-name TechnicalSupports \
    --attribute-definitions \
        AttributeName=cpf,AttributeType=S \
        AttributeName=id,AttributeType=N \
    --key-schema \
        AttributeName=cpf,KeyType=HASH \
        AttributeName=id,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000

echo "Tabela criada com sucesso!" 