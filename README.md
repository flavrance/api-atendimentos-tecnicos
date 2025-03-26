# API de Atendimentos Técnicos

API REST para gerenciamento de atendimentos técnicos, desenvolvida com Spring Boot e AWS DynamoDB.

## Tecnologias Utilizadas

- Java 21 (LTS)
- Spring Boot 3.4.2
- AWS DynamoDB
- Docker e Docker Compose
- GitHub Actions
- SpringDoc OpenAPI 2.3.0

## Pré-requisitos

- Java 21
- Docker e Docker Compose
- AWS CLI (para deploy)

## Configuração do Ambiente Local

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/api-atendimentos-tecnicos.git
cd api-atendimentos-tecnicos
```

2. Inicie o ambiente local com Docker Compose:
```bash
docker-compose up -d
```

3. Verifique os serviços:
- API: http://localhost:8080
- DynamoDB Admin: http://localhost:8001
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Docs: http://localhost:8080/api-docs

## Documentação da API

A documentação completa da API está disponível através do Swagger UI, que pode ser acessado em:
- Ambiente local: http://localhost:8080/swagger-ui.html
- Produção: https://api.empresa.com/swagger-ui.html

A especificação OpenAPI (anteriormente Swagger) está disponível em:
- Ambiente local: http://localhost:8080/api-docs
- Produção: https://api.empresa.com/api-docs

## Endpoints

### Criar Atendimento Técnico

```http
POST /api/technical-supports
Content-Type: application/json

{
    "cpf": "12345678901",
    "complaint": "Problema com internet",
    "date": "2025-03-23"
}
```

### Pesquisar Atendimentos por CPF

```http
GET /api/technical-supports
CPF: 12345678901
```

## Respostas

### Sucesso (200 OK)
```json
{
    "id": "1",
    "cpf": "12345678901",
    "complaint": "Problema com internet",
   "date" : "2025-03-23",
    "status": "A"
}
```

### Erro de Validação (422 Unprocessable Entity)
```json
{
    "timestamp": "2025-01-23T10:30:00",
    "status": 422,
    "errors": {
        "cpf": "CPF must contain 11 numeric digits"
    }
}
```

### Erro de Header Ausente (400 Bad Request)
```json
{
    "timestamp": "2025-01-23T10:30:00",
    "status": 400,
    "error": "Header CPF is required"
}
```

## Desenvolvimento

### Build e Testes

Para executar os testes:
```bash
./mvnw test
```

Para build local:
```bash
./mvnw clean package
```

### Ambiente Local com Docker

O ambiente local inclui:
- API Spring Boot
- DynamoDB Local
- DynamoDB Admin UI
- Script de inicialização da tabela

### Variáveis de Ambiente

```properties
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=local
AWS_SECRET_ACCESS_KEY=local
AWS_DYNAMODB_ENDPOINT=http://localhost:8000
```

## Deploy

O deploy é automatizado via GitHub Actions e utiliza AWS ECS (Fargate).

### Pré-requisitos para Deploy

1. Configure as secrets no GitHub:
   - AWS_ACCESS_KEY_ID
   - AWS_SECRET_ACCESS_KEY

2. Infraestrutura AWS necessária:
   - ECR Repository
   - ECS Cluster
   - ECS Service
   - DynamoDB Table

### Pipeline de CI/CD

O pipeline inclui:
1. Build e testes
2. Build da imagem Docker
3. Push para Amazon ECR
4. Deploy no ECS Fargate

## Monitoramento

- Logs disponíveis no CloudWatch
- Métricas do DynamoDB no CloudWatch
- Health check endpoint: `/actuator/health`
- Métricas da aplicação: `/actuator/metrics`
- Informações da aplicação: `/actuator/info`

## Arquitetura e Boas Práticas

### Princípios SOLID
- **Single Responsibility**: Cada classe tem uma única responsabilidade
- **Open/Closed**: Extensível para novos comportamentos sem modificar o código existente
- **Liskov Substitution**: Uso correto de interfaces e abstrações
- **Interface Segregation**: Interfaces específicas para cada cliente
- **Dependency Inversion**: Dependências injetadas via construtor

### Padrões de Projeto
- **Repository Pattern**: Abstração do acesso a dados
- **DTO Pattern**: Objetos específicos para transferência de dados
- **Factory Pattern**: Criação de objetos complexos
- **Builder Pattern**: Construção de objetos imutáveis

### Clean Architecture
- **Camadas bem definidas**: API, Application, Domain, Infrastructure
- **Dependency Rule**: Dependências apontam para dentro
- **Use Cases**: Regras de negócio isoladas
- **Entities**: Regras de negócio empresariais

## Segurança

- Validação de CPF
- Sanitização de inputs
- Tratamento adequado de erros
- Não exposição de detalhes internos nas respostas de erro
- Endpoints de monitoramento protegidos

## Suporte

Para reportar bugs ou sugerir melhorias, abra uma issue no GitHub.

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes. 