# Sistema de Votação em Assembleias de Cooperativas

API REST para gerenciamento e participação em sessões de votação em assembleias cooperativas, construída em Java com Spring Boot, PostgreSQL e Flyway.

## Decisões de Arquitetura

### Controle de Sessão de Votação

Adotei uma abordagem on-demand sem scheduler: a sessão persiste `dataAbertura` e `duracaoSegundos`, e o cálculo de "está aberta?" ocorre no momento da validação (`LocalDateTime.now() < dataAbertura + duracao`). Isso evita complexidade de jobs e estado adicional, mantendo a solução simples e previsível.

Uma alternativa seria mensageria (ex: RabbitMQ, Kafka): ao abrir uma sessão, publica-se uma mensagem com delay igual à duração, que ao vencer dispara um evento "sessão encerrada", acionando a contabilização e notificações em tempo real aos clientes. Isso seria útil em cenários com múltiplas instâncias da aplicação ou se houvesse necessidade de notificar clientes do encerramento da votação, porém, adiciona infraestrutura e complexidade operacional desproporcional ao escopo atual, onde uma validação simples por tempo é suficiente e testável sem dependências externas.
### Versionamento de API

Usei **path variable** (`/v1/pautas`) em vez de headers ou content negotiation. Isso é explícito e testável no navegador. Uma v2 conviveria em paralelo (`/v2/...`) se necessária, sem quebrar clientes na v1.

### Performance

- **Constraint única** em `(pauta_id, associado_id)` no banco garante "1 voto por pauta" mesmo sob concorrência.
- **Query agregada** (`GROUP BY`) no repository evita carregar votos em memória — o cálculo de resultado é O(log n) de lookup de índice, não O(n) em Java.

### Integração CPF (Bônus 1)

Cliente HTTP configurável (`user-info.base-url`, `user-info.habilitado`). Desabilitado por padrão (todos podem votar) — habilitar apenas se o serviço estiver disponível, evitando travamento de testes. Trata 404 e status `UNABLE_TO_VOTE` como rejeição.

## Deploy Online

A aplicação está deployada no **Railway** e disponível em:

https://cooperativa-production-b9f1.up.railway.app/

## Rodando Localmente

### Pré-requisitos

- **Docker + Docker Compose** (único pré-requisito)

O `Dockerfile` usa uma **build multi-stage**:
1. **Build stage**: `maven:3.9-eclipse-temurin-21` compila o projeto (Maven + Java 21)
2. **Run stage**: `eclipse-temurin:21-jre-alpine` executa o jar (apenas JRE, otimizado)

Quando você roda `docker compose up --build`, o container compila o código inteiro e gera o executável.

### Iniciar

```bash
docker compose up --build
```

A aplicação sobe em `http://localhost:8080`, Postgres em `localhost:5432`.

**Credenciais Postgres**: `cooperativa` / `postgres`

### Swagger UI
http://localhost:8080/swagger-ui/index.html

### Testes

```bash
# Se tiver Maven/Java 21 localmente:
mvn test
mvn test -Dtest=*Performance*

# Ou via Docker:
docker compose exec app mvn test
```

## Estrutura do Projeto
```
src/main/java/com/micahmaclean/cooperativa/
├── controller/v1/              # Endpoints REST versionados
├── service/                    # Lógica de negócio
├── repository/                 # Acesso a dados (JPA)
├── model/                      # Entidades (Pauta, Sessao, Voto)
├── dto/
│   ├── request/               # DTOs de entrada (imutáveis)
│   └── response/              # DTOs de saída (imutáveis)
├── exception/                  # Exceções de domínio
├── client/                     # Cliente HTTP externo (Bônus 1)
└── config/                     # Configurações

src/main/resources/
├── application.yml             # Configuração padrão
└── db/migration/               # Migrações Flyway (V1, V2, V3)

src/test/java/com/micahmaclean/cooperativa/
├── service/                    # Testes unitários (Mockito)
└── repository/                 # Testes de integração (Testcontainers)
```
## Endpoints

### Pautas
- `POST /v1/pautas` — Cadastra pauta
- `GET /v1/pautas/{id}` — Consulta pauta
- `PUT /v1/pautas/{id}` — Edita pauta (apenas se sem sessão aberta)

### Sessões

- `POST /v1/pautas/{id}/sessoes` — Abre sessão (duração opcional, default 60s)
- `GET /v1/pautas/{id}/sessoes` — Consulta sessão

### Votos

- `POST /v1/pautas/{id}/votos` — Registra voto (com validação de elegibilidade)
- `GET /v1/pautas/{id}/votos/resultado` — Contabiliza resultado

## Tratamento de Erros

Todas as respostas de erro seguem o mesmo formato:

```json
{
  "timestamp": "2026-07-16T12:38:05.948Z",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Pauta não encontrada: 3fa85f64-5717-..."
}
```

Códigos HTTP:
- `400 Bad Request` — Validação falhou ou sessão encerrada
- `404 Not Found` — Recurso não existe
- `409 Conflict` — Voto duplicado ou sessão já existe

## Configurações

`application.yml`:

```yaml
server:
  port: ${PORT:8080}
  forward-headers-strategy: framework

spring:
  application:
    name: cooperativa
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/cooperativa}
    username: ${DB_USERNAME:cooperativa}
    password: ${DB_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
  flyway:
    enabled: true
    locations: classpath:db/migration

user-info:
  base-url: https://user-info.herokuapp.com
  habilitado: false
```
