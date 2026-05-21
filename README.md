# Enterprise Document Q&A Backend

This backend repository implements a production-grade microservices architecture for an enterprise document Q&A platform using RAG, Spring Boot, Kafka, PostgreSQL, Redis, and OpenAI.

## Architecture

- `common` — shared DTOs, Kafka event definitions, security helpers, and domain models
- `api-gateway-service` — reactive gateway routing and edge authorization
- `auth-service` — JWT authentication, refresh tokens, and RBAC
- `document-ingestion-service` — file upload, metadata storage, and document ingestion events
- `embedding-service` — document chunk embeddings using OpenAI APIs
- `vector-search-service` — semantic search and pgvector-backed similarity queries
- `ai-orchestrator-service` — RAG orchestration, prompt templating, and LLM completion
- `chat-service` — chat history persistence and conversation memory
- `analytics-service` — aggregated metrics, Kafka topic health, and observability
- `kafka-event-processor-service` — asynchronous consumer orchestration and DLQ handling

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker / Docker Compose
- Kubernetes (kubectl)
- PostgreSQL with `pgvector` extension
- Kafka cluster
- Redis
- OpenAI API key configured using environment variables

### Local development

```bash
cd backend
mvn -pl common,auth-service,document-ingestion-service,embedding-service,vector-search-service,ai-orchestrator-service,chat-service,analytics-service,kafka-event-processor-service -am clean package
```

### Run with Docker Compose

```bash
cd backend
docker compose up --build
```

### Kubernetes

Use the manifests in `backend/k8s/` or deploy with the Helm chart in `backend/helm/enterprise-docqa`.
