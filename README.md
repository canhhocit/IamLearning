# Hands-on Learning Lab

A task tracker for learning React, Spring Boot, Playwright, Kafka, PostgreSQL, Docker, and Kubernetes by building and experimenting.

## Project structure

```text
backend/         Spring Boot REST API with PostgreSQL and Kafka
  com.iamlearning/
    tasks/       Single business module, split into api, application, domain, infrastructure
frontend/        React + Vite web application
PlayWright-e2e/  Independent Playwright end-to-end test project
k8s/             Kubernetes Deployments and Services
docker-compose.yml  Kafka and PostgreSQL development infrastructure only
.env.example     Docker Compose settings template
backend/.env.example
frontend/.env.example
PlayWright-e2e/.env.example
.github/workflows/ci.yml
```

## Backend architecture

The backend is one deployable Spring Boot application, organized as a modular monolith. The `tasks` module owns its HTTP API, use cases, domain model, PostgreSQL adapter, and Kafka integration:

```text
tasks/
  api/             HTTP controllers and request/response DTOs
  application/     Use cases and outbound ports
  domain/          Business model, repository port, domain events
  infrastructure/  PostgreSQL and Kafka adapters
```

Dependencies point inward: the API calls application use cases; application code depends on domain contracts; infrastructure implements those contracts. The domain does not depend on Spring, JPA, or Kafka. Add future business areas as sibling modules instead of letting modules reach into each other's infrastructure.

## First steps: start infrastructure, then run the apps locally

Prerequisites: Docker Desktop with its Linux engine running, Java 17+, Maven, and Node.js 20+.

1. Create a local environment file for each project:

   ```powershell
   Copy-Item .env.example .env
   Copy-Item backend\.env.example backend\.env
   Copy-Item frontend\.env.example frontend\.env
   Copy-Item PlayWright-e2e\.env.example PlayWright-e2e\.env
   ```

   Each `.env` is ignored by Git. The sample database credentials are for local learning only; do not reuse them elsewhere.

2. Start **only Kafka and PostgreSQL** in Docker:

   ```powershell
   docker compose up -d kafka postgres
   docker compose ps
   ```

   Wait until both services report `healthy`. PostgreSQL stores task data; Kafka handles `task.created` events.

3. Start the backend manually in a terminal:

   ```powershell
   cd backend
   mvn spring-boot:run
   ```

   Spring Boot reads `backend/.env`. The API is at `http://localhost:8080`.

4. Start the frontend manually in a second terminal:

   ```powershell
   cd frontend
   npm.cmd install
   npm.cmd run dev
   ```

   Vite reads `frontend/.env`, serves at `http://localhost:5173`, and proxies `/api` requests to the backend.

5. In a third terminal, set up and run the separate Playwright project:

   ```powershell
   cd PlayWright-e2e
   npm.cmd install
   npx.cmd playwright install chromium
   npm.cmd test
   ```

   Playwright reads `PlayWright-e2e/.env`, reuses the already-running app, or starts the backend and frontend itself if they are not running. The test creates, completes, and deletes a task.

Stop the manually started apps with `Ctrl+C`. Stop Kafka and PostgreSQL when finished:

```powershell
docker compose down
```

PostgreSQL data lives in a named Docker volume and survives `docker compose down`. To intentionally remove that local learning data, use `docker compose down -v`.

## Environment configuration

Configuration is scoped per project: root `.env` is for Docker Compose; `backend/.env` is for Spring Boot; `frontend/.env` is for Vite; and `PlayWright-e2e/.env` is for Playwright. Keep the local service addresses aligned when changing ports (Kafka and PostgreSQL in the backend env, and the frontend/backend URLs in the E2E env).

Each project has its own `.env.example`; copy the matching example before running that project. Docker Compose automatically reads only the root `.env`.

## Continuous integration

GitHub Actions runs [`.github/workflows/ci.yml`](.github/workflows/ci.yml) on every push and pull request. It starts Kafka and PostgreSQL, runs the backend tests, builds the frontend, and runs the Playwright end-to-end test. CI creates ignored `.env` files from the checked-in examples.

## Kubernetes

Prerequisites: a Kubernetes cluster and `kubectl`. The current manifests are an introductory deployment scaffold; the local Compose setup is the supported way to start Kafka and PostgreSQL for the manually run app.

For Minikube, build the app images into its image store and apply the manifests:

```powershell
minikube image build -t task-tracker-backend:local -f backend/Dockerfile backend
minikube image build -t task-tracker-frontend:local -f frontend/Dockerfile frontend
kubectl apply -f k8s/
kubectl get pods,services
```

The Kubernetes manifests are minimal learning examples, not production deployment settings.

## Learning roadmap

- [ ] **React + REST:** follow the task list flow from the UI to the Spring Boot controller and back.
- [ ] **PostgreSQL:** trace a task through `TaskApplicationService` and the domain `TaskRepository` port; inspect the generated table and persistent data.
- [ ] **Playwright:** inspect `PlayWright-e2e/playwright.config.js` and `PlayWright-e2e/tests/tasks.spec.js`; experiment with locators, assertions, and test isolation.
- [ ] **Kafka:** trace the `task.created` event from `TaskController` through `TaskEventPublisher` to `TaskEventConsumer`; inspect topic, key, payload, and consumer group.
- [ ] **Docker:** inspect `docker-compose.yml`; experiment with the Kafka and PostgreSQL services and their environment variables.
- [ ] **Kubernetes:** inspect `k8s/`; learn Deployments, Services, health probes, image loading, and scaling.

Build each step on the same app to see how the tools fit together. The goal is to write and configure the integrations yourself, not to reimplement Playwright, Kafka, or Kubernetes.

## API

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/tasks` | List tasks |
| `POST` | `/api/tasks` | Create a task with `{ "title": "Learn Playwright" }`; stores it in PostgreSQL and publishes a `task.created` Kafka event |
| `PATCH` | `/api/tasks/{id}` | Set completion with `{ "completed": true }` |
| `DELETE` | `/api/tasks/{id}` | Delete a task |

Run backend tests from `backend/` with `mvn test`. Build the frontend from `frontend/` with `npm.cmd run build` on Windows.
