# API Endpoints & Example curl Requests

This document lists the primary HTTP endpoints exposed by the backend microservices and provides example curl requests you can use to test them locally. These examples assume you started the stack with Docker Compose on the same machine and the services are available at the ports configured in `docker-compose.yml`.

Base URLs (local Docker Compose mappings)
- API Gateway: http://localhost:8080
- Auth service: http://localhost:8081
- Document Ingestion: http://localhost:8082
- Embedding service: http://localhost:8083
- Vector Search: http://localhost:8084
- AI Orchestrator: http://localhost:8085
- Chat service: http://localhost:8086
- Analytics service: http://localhost:8087
- Kafka event processor (if needed): http://localhost:8088

---

## 1) API Gateway

### Health
- GET /api/gateway/health

curl:
```bash
curl -v http://localhost:8080/api/gateway/health
```

### Proxy a document via gateway (forwards to document service)
- GET /api/gateway/documents/{id}

curl:
```bash
curl -v http://localhost:8080/api/gateway/documents/<DOCUMENT_ID>
```

---

## 2) Auth Service
Base: http://localhost:8081

### Login
- POST /api/auth/login
- Request JSON: { "username": "user", "password": "pass" }
- Returns: JSON with access_token and refresh_token

curl:
```bash
curl -v -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

### Refresh access token
- POST /api/auth/refresh
- Body: raw refresh token (string)

curl:
```bash
curl -v -X POST http://localhost:8081/api/auth/refresh \
  -H "Content-Type: text/plain" \
  --data-raw "<REFRESH_TOKEN>"
```

Note: Protected endpoints in other services expect `Authorization: Bearer <ACCESS_TOKEN>`.

---

## 3) Document Ingestion Service
Base: http://localhost:8082

### Upload document (multipart file)
- POST /api/documents/upload
- Content-Type: multipart/form-data, part name `file`

curl:
```bash
curl -v -X POST http://localhost:8082/api/documents/upload \
  -F "file=@/path/to/mydoc.pdf" \
  -H "Authorization: Bearer <ACCESS_TOKEN>"   # if your service requires auth
```

Response: 201 Created with a JSON StandardResponse containing document id and metadata.

---

## 4) Embedding Service
Base: http://localhost:8083

(This service likely calls OpenAI; ensure `OPENAI_API_KEY` is set)

No public controller endpoints included in this code snapshot; embedding is invoked internally by services through clients.

---

## 5) Vector Search Service
Base: http://localhost:8084

### Search relevant chunks
- GET /api/search/relevant?queryEmbedding=<comma or repeated param>&limit=5
- `queryEmbedding` expects multiple float values. Example uses repeated `queryEmbedding` params.

Example curl (short vector):
```bash
curl -v "http://localhost:8084/api/search/relevant?queryEmbedding=0.12&queryEmbedding=0.34&queryEmbedding=0.56&limit=3"
```

Note: If sending a long list, you can URL-encode or use repeated parameters.

---

## 6) AI Orchestrator Service
Base: http://localhost:8085

### Query (RAG chat endpoint)
- POST /api/chat/query
- Body: JSON matching ChatRequest (e.g. { "question": "...", ... })

Example curl:
```bash
curl -v -X POST http://localhost:8085/api/chat/query \
  -H "Content-Type: application/json" \
  -d '{"question":"What is the company policy for data retention?"}'
```

Response: JSON ChatResponse (answer, source, latency)

---

## 7) Chat Service
Base: http://localhost:8086

### Get chat history for a user
- GET /api/chat/history?userId=<userId>

curl:
```bash
curl -v "http://localhost:8086/api/chat/history?userId=alice"
```

### Get a specific conversation
- GET /api/chat/history/{conversationId}

curl:
```bash
curl -v http://localhost:8086/api/chat/history/<CONVERSATION_ID>
```

---

## 8) Analytics Service
Base: http://localhost:8087

### Metrics / stats
- GET /api/analytics

curl:
```bash
curl -v http://localhost:8087/api/analytics
```

### Kafka status
- GET /api/analytics/kafka/status

curl:
```bash
curl -v http://localhost:8087/api/analytics/kafka/status
```

---

## 9) Kafka Event Processor Service
Base: http://localhost:8088

This service is typically an internal consumer; no HTTP endpoints were found in the provided snapshot.

---

## Authorization
- When endpoints require authentication, add header:
  -H "Authorization: Bearer <ACCESS_TOKEN>"

## Troubleshooting & tips
- If you get `401 Unauthorized` when hitting protected endpoints, first call `/api/auth/login` to obtain a token.
- Ensure `OPENAI_API_KEY` is set in the `.env` when using embedding/ai endpoints.
- If a service returns `500` at startup, check its container logs:
  ```bash
  docker compose logs -f <service-name>
  ```

---

## Quick test sequence (recommended)
1. Build and start stack
```bash
cd "D:/AI Project/avi-rag-doc-analyzer-backend"
docker compose up --build -d
```
2. Confirm services are healthy
```bash
docker compose ps
```
3. Login and test vector search or upload
```bash
# Login
curl -v -X POST http://localhost:8081/api/auth/login -H "Content-Type: application/json" -d '{"username":"admin","password":"admin"}'

# Use returned access token in subsequent calls
```

---

If you want, I can:
- Add more precise example JSON request bodies for chat/document upload if you share the DTO shape (e.g., `ChatRequest` fields), or
- Run a few test curl calls from this environment and show the responses (requires services to be up and network access available). 

Would you like me to add DTO-based example payloads (I can inspect DTO classes and include exact fields)?

