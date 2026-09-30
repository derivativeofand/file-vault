# File Vault

File Vault is a secure file storage and sharing REST API built with Java and Spring Boot. Users can register, log in, and upload files that only they can access. It also includes a Retrieval-Augmented Generation (RAG) pipeline that lets users ask natural language questions about the content of their uploaded documents.

## Features

- **User authentication** — register and log in with JWT-based authentication
- **Secure file storage** — upload, list, and delete files; each file is scoped to its owner
- **Role-based access control** — regular users only see their own files; admins can manage all users and files
- **AI-powered document search (RAG)** — ask questions about uploaded PDFs and text files and get AI-generated answers grounded in the document's actual content
- **Vector search** — document chunks are embedded and stored in PostgreSQL using pgvector, with automatic cleanup when a file is deleted

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, Spring Data JPA
- **Database:** PostgreSQL
- **Auth:** JWT (JSON Web Tokens)
- **AI/RAG:** Spring AI, Ollama (local LLM inference), pgvector, Apache Tika (document parsing)
- **Build tool:** Maven

## Installation

### Prerequisites

- Java 21+
- Maven
- PostgreSQL with the `pgvector` extension enabled
- [Ollama](https://ollama.com) installed locally

### Pull the required Ollama models

```bash
ollama pull llama3.2
ollama pull nomic-embed-text
```

### Set up the database

```bash
sudo -u postgres psql
```

```sql
CREATE DATABASE filevault;
CREATE USER filevaultuser WITH PASSWORD 'yourpassword';
GRANT ALL PRIVILEGES ON DATABASE filevault TO filevaultuser;
\c filevault
CREATE EXTENSION vector;
```

### Configure the application

Update `src/main/resources/application.properties` with your database credentials and JWT secret:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/filevault
spring.datasource.username=filevaultuser
spring.datasource.password=yourpassword

jwt.secret=your-base64-encoded-secret
jwt.expiration=86400000

spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2
spring.ai.ollama.embedding.options.model=nomic-embed-text
```

### Run the application

```bash
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`.

## Usage

### Register a user

```bash
curl -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"jun","email":"jun@example.com","password":"mypassword"}'
```

### Log in

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"jun","password":"mypassword"}'
```

Returns a JWT token. Use it as a Bearer token on every subsequent request.

### Upload a file

```bash
curl -X POST http://localhost:8080/files \
  -H "Authorization: Bearer <your-token>" \
  -F "file=@document.pdf"
```

### Ask a question about a file

```bash
curl -X POST http://localhost:8080/chat/5 \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: text/plain" \
  -d "What is the security deposit amount?"
```

### List your files

```bash
curl http://localhost:8080/files \
  -H "Authorization: Bearer <your-token>"
```

### Delete a file

```bash
curl -X DELETE http://localhost:8080/files/5 \
  -H "Authorization: Bearer <your-token>"
```

## Architecture

The project follows a layered architecture:

```
Controller → Service → Repository → Database
```

RAG-specific processing follows its own pipeline, triggered after a successful file upload:

```
File upload → Text extraction (Apache Tika) → Chunking (Spring AI TokenTextSplitter)
            → Embedding (Ollama nomic-embed-text) → Storage (pgvector)
```

When a user asks a question, relevant chunks are retrieved via vector similarity search, scoped to the specific file, and passed to the AI model (Ollama llama3.2) as context to generate a grounded answer.

## Roadmap

- [ ] File sharing between users
- [ ] Shareable links with expiration
- [ ] Response DTOs to fully decouple API responses from internal entities

## License

[MIT](https://choosealicense.com/licenses/mit/)
