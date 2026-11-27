# Báo cáo công việc - Refactor cấu trúc và chuẩn hóa đặt tên

## 1. Thông tin chung
- **Ngày thực hiện:** 2026-11-27
- **Tính năng / Nghiệp vụ:** Refactor cấu trúc package, tách lớp trách nhiệm và chuẩn hóa quy tắc đặt tên cho Backend (Spring Boot) & Frontend (React)
- **Người thực hiện:** Antigravity AI Assistant

## 2. Mục tiêu & Yêu cầu
- Chuẩn hóa cấu trúc thư mục và tên package Java theo quy ước chuẩn Spring Boot / Java (số ít cho tên package, tách biệt ranh giới Controller – Service – Entity).
- Chuẩn hóa đặt tên class, method, DTO và thành phần Kafka messaging cho nhất quán.
- Tổ chức lại mã nguồn Frontend theo vai trò (`api/`, `components/`) nhưng vẫn giữ nguyên hành vi UI và contract API để không ảnh hưởng kiểm thử E2E.

## 3. Chi tiết thay đổi

### Backend (`backend/`)
1. **Chuẩn hóa tên package sang số ít (Java convention):**
   - `com.iamlearning.tasks.*` → `com.iamlearning.task.*` (`controller`, `service`, `dto.request`, `dto.response`, `entity`, `event`, `repository`).
   - `com.iamlearning.shared.exceptions` → `com.iamlearning.shared.exception`.
2. **Tách biệt ranh giới Controller – Service – Entity:**
   - `TaskService` nhận DTO (`CreateTaskRequest`, `UpdateTaskCompletionRequest`) và trả về `TaskResponse`, không để JPA Entity (`Task`) rò rỉ lên tầng `TaskController`.
   - Chuẩn hóa tên phương thức: `getAllTasks()`, `createTask(...)`, `updateTaskCompletion(...)`, `deleteTask(...)`.
3. **Chuẩn hóa DTO & Validation:**
   - Đổi `SetTaskCompletionRequest(boolean completed)` → `UpdateTaskCompletionRequest(@NotNull Boolean completed)` và bổ sung `@Valid` ở `TaskController.updateTaskCompletion(...)` để từ chối payload thiếu trường `completed` với HTTP 400.
4. **Chuẩn hóa Kafka Messaging & Exception Handling:**
   - Tách hằng số topic ra `KafkaTopics` (`TASK_CREATED = "task.created"`).
   - Đổi `KafkaTopicConfiguration` → `KafkaTopicConfig`, `TaskCreatedEventConsumer` → `KafkaTaskEventConsumer` để đồng bộ với `KafkaTaskEventPublisher`.
   - Chuẩn hóa tên phương thức trong `GlobalExceptionHandler` (`handleResourceNotFound`, `handleMethodArgumentNotValid`, `handleMalformedRequest`, `buildErrorResponse`).
5. **Cập nhật cấu hình & Unit Test:**
   - Cập nhật `spring.json.trusted.packages` và `spring.json.value.default.type` trong `application.yml` sang `com.iamlearning.task.event`.
   - Di chuyển và cập nhật `TaskServiceTest` sang `com.iamlearning.task.service`, bổ sung test case `deletesExistingTask`.

### Frontend (`frontend/`)
1. **Tổ chức lại thư mục `src/`:**
   - Chuyển `src/api.js` → `src/api/taskApi.js` (chuẩn hóa tên `taskApi`, các hàm `getAll`, `create`, `updateCompletion`, `remove`).
   - Tách `App.jsx` thành các component đơn trách nhiệm trong `src/components/`: `TaskForm.jsx`, `TaskList.jsx`, `TaskItem.jsx`.
   - Chuẩn hóa tên handler trong `App.jsx`: `handleAddTask`, `handleToggleTask`, `handleDeleteTask`.

### Tài liệu (`README.md`)
- Cập nhật sơ đồ kiến trúc và tên package/class tương ứng với cấu trúc mới.

## 4. Danh mục tệp đã chỉnh sửa / tạo mới
- [ResourceNotFoundException.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/shared/exception/ResourceNotFoundException.java)
- [ApiErrorResponse.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/shared/exception/ApiErrorResponse.java)
- [GlobalExceptionHandler.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/shared/exception/GlobalExceptionHandler.java)
- [Task.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/entity/Task.java)
- [TaskRepository.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/repository/TaskRepository.java)
- [CreateTaskRequest.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/dto/request/CreateTaskRequest.java)
- [UpdateTaskCompletionRequest.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/dto/request/UpdateTaskCompletionRequest.java)
- [TaskResponse.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/dto/response/TaskResponse.java)
- [TaskCreatedEvent.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/event/TaskCreatedEvent.java)
- [TaskEventPublisher.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/event/TaskEventPublisher.java)
- [TaskService.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/service/TaskService.java)
- [TaskController.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/task/controller/TaskController.java)
- [KafkaTopics.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/messaging/kafka/KafkaTopics.java)
- [KafkaTopicConfig.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/messaging/kafka/KafkaTopicConfig.java)
- [KafkaTaskEventPublisher.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/messaging/kafka/KafkaTaskEventPublisher.java)
- [KafkaTaskEventConsumer.java](file:///C:/personal/IamLearning/backend/src/main/java/com/iamlearning/messaging/kafka/KafkaTaskEventConsumer.java)
- [application.yml](file:///C:/personal/IamLearning/backend/src/main/resources/application.yml)
- [TaskServiceTest.java](file:///C:/personal/IamLearning/backend/src/test/java/com/iamlearning/task/service/TaskServiceTest.java)
- [taskApi.js](file:///C:/personal/IamLearning/frontend/src/api/taskApi.js)
- [TaskForm.jsx](file:///C:/personal/IamLearning/frontend/src/components/TaskForm.jsx)
- [TaskItem.jsx](file:///C:/personal/IamLearning/frontend/src/components/TaskItem.jsx)
- [TaskList.jsx](file:///C:/personal/IamLearning/frontend/src/components/TaskList.jsx)
- [App.jsx](file:///C:/personal/IamLearning/frontend/src/App.jsx)
- [README.md](file:///C:/personal/IamLearning/README.md)

## 5. Kết quả kiểm thử & xác minh
- **Backend (`mvn --batch-mode test`):** `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0` — BUILD SUCCESS.
- **Frontend (`npm.cmd run build`):** Vite build thành công (`dist/` được tạo sạch sẽ, không lỗi).
- **Playwright E2E (`PlayWright-e2e`):** Chưa chạy do phụ thuộc Docker Kafka & PostgreSQL đang chạy cục bộ.

## 6. Tồn đọng & Đề xuất tiếp theo
- Thư mục `PlayWright-e2e/` đang viết hoa chữ `W` (`PlayWright`); có thể cân nhắc đổi sang `playwright-e2e/` hoặc `e2e/` nếu muốn đồng bộ kebab-case, lưu ý cần cập nhật kèm `.github/workflows/ci.yml` và `README.md`.
- Khi chạy lại môi trường Docker Compose, nên chạy `npm.cmd test` trong `PlayWright-e2e/` để xác nhận luồng E2E toàn phần.
