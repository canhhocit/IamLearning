# Hands-on Learning Lab

A task tracker for learning React, Spring Boot, Playwright, Kafka, Docker, and Kubernetes by building and experimenting.

## Project structure

```text
backend/     Spring Boot REST API, Kafka producer and consumer
frontend/    React + Vite web application
PlayWright-e2e/ Independent Playwright end-to-end test project
k8s/         Kubernetes Deployments and Services
docker-compose.yml   Local Kafka, backend, and frontend stack
```

Tasks are kept in backend memory, so they reset when the backend restarts. Kafka runs as a single-node development broker; Kubernetes uses a single Kafka pod with ephemeral storage. These are learning setups, not production configurations.

## Run the full stack with Docker Compose

Prerequisite: Docker Desktop with Compose.

From the repository root:

```powershell
docker compose up --build
```

Open `http://localhost:5173`. Create a task and watch the backend logs for the `task.created` event being consumed:

```powershell
docker compose logs -f backend
```

Stop the stack with `Ctrl+C`, or run `docker compose down` in another terminal.

## Run locally and try Playwright

Prerequisites: Java 17+, Maven, Node.js 20+, and Docker.

1. Start the Kafka broker:

   ```powershell
   docker compose up -d kafka
   ```

2. Install frontend dependencies from `frontend/`:

   ```powershell
   cd frontend
   npm.cmd install
   ```

3. Install Playwright and its browser from `PlayWright-e2e/`:

   ```powershell
   cd ..\PlayWright-e2e
   npm.cmd install
   npx.cmd playwright install chromium
   ```

4. Run the end-to-end test from `PlayWright-e2e/`. Playwright starts the Spring Boot API and Vite frontend for the test run:

   ```powershell
   npm.cmd test
   ```

To run the pieces manually, keep Kafka running, start `mvn spring-boot:run` from `backend/`, then start `npm.cmd run dev` from `frontend/`. Open `http://localhost:5173`.

## Run on Kubernetes

Prerequisites: a Kubernetes cluster and `kubectl`. Build the images in the cluster's local image environment so the manifests can use their `:local` tags. For Minikube:

```powershell
minikube image build -t task-tracker-backend:local -f backend/Dockerfile backend
minikube image build -t task-tracker-frontend:local -f frontend/Dockerfile frontend
kubectl apply -f k8s/
kubectl get pods,services
```

Open the frontend at `http://localhost:30080` when the cluster exposes NodePort services locally (for example, Minikube). The manifests define a single Kafka broker and ephemeral task data. They are intentionally minimal for learning; do not use them as production deployment settings.

## Learning roadmap

- [ ] **React + REST:** follow the task list flow from the UI to the Spring Boot controller and back.
- [ ] **Playwright:** inspect `PlayWright-e2e/playwright.config.js` and `PlayWright-e2e/tests/tasks.spec.js`; experiment with locators, assertions, and test isolation. Playwright dependencies stay separate from the frontend.
- [ ] **Kafka:** trace the `task.created` event from `TaskController` through `TaskEventPublisher` to `TaskEventConsumer`; inspect topic, key, payload, and consumer group.
- [ ] **Docker:** inspect both Dockerfiles and `docker-compose.yml`; change a service and rebuild it.
- [ ] **Kubernetes:** inspect `k8s/`; learn Deployments, Services, health probes, image loading, and scaling.

Build each step on the same app to see how the tools fit together. The goal is to write and configure the integrations yourself, not to reimplement Playwright, Kafka, or Kubernetes.

## API

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/tasks` | List tasks |
| `POST` | `/api/tasks` | Create a task with `{ "title": "Learn Playwright" }`; publishes a `task.created` Kafka event |
| `PATCH` | `/api/tasks/{id}` | Set completion with `{ "completed": true }` |
| `DELETE` | `/api/tasks/{id}` | Delete a task |

Run backend tests from `backend/` with `mvn test`. Build the frontend from `frontend/` with `npm.cmd run build` on Windows.
