import { useEffect, useState } from "react";
import { taskApi } from "./api/taskApi";
import TaskForm from "./components/TaskForm";
import TaskList from "./components/TaskList";
import UserForm from "./components/UserForm";
import UserTable from "./components/UserTable";
import { userApi } from "./api/userApi";
import type { Task, User } from "./types";

export default function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [error, setError] = useState("");
  const [users, setUsers] = useState<User[]>([]);
  const [usersLoading, setUsersLoading] = useState(true);
  const [usersError, setUsersError] = useState("");
  const [userActionNotice, setUserActionNotice] = useState("");

  useEffect(() => {
    taskApi
      .getAll()
      .then(setTasks)
      .catch(() => setError("Could not load tasks. Is the API running?"));

    userApi
      .getAll()
      .then((loadedUsers) => {
        setUsers((currentUsers) => {
          const usersById = new Map(loadedUsers.map((user) => [user.id, user]));
          currentUsers.forEach((user) => usersById.set(user.id, user));
          return Array.from(usersById.values());
        });
      })
      .catch(() => setUsersError("Could not load users. Is the API running?"))
      .finally(() => setUsersLoading(false));
  }, []);

  function handleUserCreated(user: User) {
    setUsers((currentUsers) => [
      user,
      ...currentUsers.filter((currentUser) => currentUser.id !== user.id),
    ]);
    setUserActionNotice("");
  }

  async function handleUpdateUser(user: User): Promise<void> {
    const updatedUser = await userApi.update(user);
    setUsers((currentUsers) =>
      currentUsers.map((currentUser) =>
        currentUser.id === updatedUser.id ? updatedUser : currentUser,
      ),
    );
    setUserActionNotice("User updated.");
  }

  async function handleDeleteUser(id: string): Promise<void> {
    await userApi.remove(id);
    setUsers((currentUsers) => currentUsers.filter((user) => user.id !== id));
    setUserActionNotice("User deleted.");
  }

  async function handleAddTask(title: string): Promise<boolean> {
    try {
      const task = await taskApi.create(title);
      setTasks((current) => [...current, task]);
      setError("");
      return true;
    } catch {
      setError("Could not add the task. Please try again.");
      return false;
    }
  }

  async function handleToggleTask(task: Task): Promise<void> {
    try {
      const updatedTask = await taskApi.updateCompletion(task.id, !task.completed);
      setTasks((current) =>
        current.map((item) => (item.id === updatedTask.id ? updatedTask : item)),
      );
      setError("");
    } catch {
      setError("Could not update the task. Please try again.");
    }
  }

  async function handleDeleteTask(task: Task): Promise<void> {
    try {
      await taskApi.remove(task.id);
      setTasks((current) => current.filter((item) => item.id !== task.id));
      setError("");
    } catch {
      setError("Could not delete the task. Please try again.");
    }
  }

  const completedCount = tasks.filter((task) => task.completed).length;
  const completionRate =
    tasks.length === 0 ? 0 : Math.round((completedCount / tasks.length) * 100);

  return (
    <main className="page">
      <div className="ambient-glow ambient-glow--cyan" aria-hidden="true" />
      <div className="ambient-glow ambient-glow--indigo" aria-hidden="true" />

      <div className="shell">
        <header className="hero-header">
          <div className="hero-copy">
            <div className="eyebrow-row">
              <span className="live-dot" aria-hidden="true" />
              <p className="eyebrow">HANDS-ON LEARNING LAB</p>
            </div>
            <h1 id="page-title">Small steps, real skills.</h1>
            <p className="intro">
              A full-stack workspace to explore React, Spring Boot, PostgreSQL,
              Kafka, and Playwright in real time.
            </p>
          </div>

          <div className="kpi-strip" aria-label="Workspace summary">
            <div className="kpi-card kpi-card--cyan">
              <span className="kpi-label">LEARNERS</span>
              <strong className="kpi-value">{users.length}</strong>
              <span className="kpi-meta">Active profiles</span>
            </div>
            <div className="kpi-card kpi-card--emerald">
              <span className="kpi-label">TASKS DONE</span>
              <strong className="kpi-value">
                {completedCount}/{tasks.length}
              </strong>
              <span className="kpi-meta">Completed items</span>
            </div>
            <div className="kpi-card kpi-card--indigo">
              <span className="kpi-label">PROGRESS</span>
              <strong className="kpi-value">{completionRate}%</strong>
              <div className="kpi-progress-track" aria-hidden="true">
                <div
                  className="kpi-progress-fill"
                  style={{ width: `${completionRate}%` }}
                />
              </div>
            </div>
          </div>
        </header>

        <div className="dashboard-grid">
          <section
            className="glass-panel user-section"
            aria-labelledby="user-section-title"
          >
            <div className="section-heading">
              <div>
                <p className="section-kicker">USER MODULE</p>
                <h2 id="user-section-title">Create a user</h2>
              </div>
              <span className="endpoint-badge">POST /api/users</span>
            </div>
            <p className="section-description">
              Add a learner profile with a name and email address.
            </p>

            <UserForm onUserCreated={handleUserCreated} />

            <div className="user-list-heading">
              <h3>Registered users</h3>
              <span className="endpoint-badge">GET /api/users</span>
            </div>

            <UserTable
              users={users}
              isLoading={usersLoading}
              error={usersError}
              actionNotice={userActionNotice}
              onUpdateUser={handleUpdateUser}
              onDeleteUser={handleDeleteUser}
            />
          </section>

          <section
            className="glass-panel task-section"
            aria-labelledby="task-section-title"
          >
            <div className="section-heading">
              <div>
                <p className="section-kicker">TASK TRACKER</p>
                <h2 id="task-section-title">Add a learning task</h2>
              </div>
              <span className="endpoint-badge">POST /api/tasks</span>
            </div>
            <p className="section-description">
              Track milestones, toggle completion, and publish Kafka events.
            </p>

            <TaskForm onAddTask={handleAddTask} />

            {error && (
              <p className="error-message" role="alert">
                {error}
              </p>
            )}

            <div className="list-heading">
              <h2>Your learning tasks</h2>
              <span className="count-pill">
                {completedCount}/{tasks.length} done
              </span>
            </div>

            <TaskList
              tasks={tasks}
              onToggleTask={handleToggleTask}
              onDeleteTask={handleDeleteTask}
            />
          </section>
        </div>

        <footer className="footer-note">Built to learn by doing.</footer>
      </div>
    </main>
  );
}
