import { useEffect, useState } from "react";
import { taskApi } from "./api/taskApi.js";
import TaskForm from "./components/TaskForm.jsx";
import TaskList from "./components/TaskList.jsx";
import UserForm from "./components/UserForm.jsx";

export default function App() {
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    taskApi
      .getAll()
      .then(setTasks)
      .catch(() => setError("Could not load tasks. Is the API running?"));
  }, []);

  async function handleAddTask(title) {
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

  async function handleToggleTask(task) {
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

  async function handleDeleteTask(task) {
    try {
      await taskApi.remove(task.id);
      setTasks((current) => current.filter((item) => item.id !== task.id));
      setError("");
    } catch {
      setError("Could not delete the task. Please try again.");
    }
  }

  const completedCount = tasks.filter((task) => task.completed).length;

  return (
    <main className="page">
      <section className="task-card" aria-labelledby="page-title">
        <p className="eyebrow">HANDS-ON LEARNING LAB</p>
        <h1 id="page-title">Small steps, real skills.</h1>
        <p className="intro">
          A simple task tracker to explore React, Spring Boot, and the tools
          around them.
        </p>

        <section className="user-section" aria-labelledby="user-section-title">
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
          <UserForm />
        </section>

        <div className="task-section-heading">
          <h2>Add a learning task</h2>
        </div>
        <TaskForm onAddTask={handleAddTask} />

        {error && (
          <p className="error-message" role="alert">
            {error}
          </p>
        )}

        <div className="list-heading">
          <h2>Your learning tasks</h2>
          <span>
            {completedCount}/{tasks.length} done
          </span>
        </div>

        <TaskList
          tasks={tasks}
          onToggleTask={handleToggleTask}
          onDeleteTask={handleDeleteTask}
        />

        <p className="footer-note">Built to learn by doing.</p>
      </section>
    </main>
  );
}
