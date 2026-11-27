import { useEffect, useState } from "react";
import { tasksApi } from "./api.js";

export default function App() {
  const [tasks, setTasks] = useState([]);
  const [title, setTitle] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    tasksApi
      .list()
      .then(setTasks)
      .catch(() => setError("Could not load tasks. Is the API running?"));
  }, []);

  async function addTask(event) {
    event.preventDefault();
    const trimmedTitle = title.trim();
    if (!trimmedTitle) return;

    try {
      const task = await tasksApi.create(trimmedTitle);
      setTasks((current) => [...current, task]);
      setTitle("");
      setError("");
    } catch {
      setError("Could not add the task. Please try again.");
    }
  }

  async function toggleTask(task) {
    try {
      const updatedTask = await tasksApi.setCompleted(task.id, !task.completed);
      setTasks((current) =>
        current.map((item) => (item.id === updatedTask.id ? updatedTask : item)),
      );
      setError("");
    } catch {
      setError("Could not update the task. Please try again.");
    }
  }

  async function deleteTask(task) {
    try {
      await tasksApi.remove(task.id);
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

        <form className="task-form" onSubmit={addTask}>
          <label className="visually-hidden" htmlFor="task-title">
            New task
          </label>
          <input
            id="task-title"
            maxLength={200}
            onChange={(event) => setTitle(event.target.value)}
            placeholder="What do you want to learn?"
            value={title}
          />
          <button type="submit">Add task</button>
        </form>

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

        {tasks.length === 0 ? (
          <p className="empty-state">No tasks yet. Add one to get started.</p>
        ) : (
          <ul className="task-list">
            {tasks.map((task) => (
              <li className="task-item" key={task.id}>
                <label className={task.completed ? "task-title completed" : "task-title"}>
                  <input
                    checked={task.completed}
                    onChange={() => toggleTask(task)}
                    type="checkbox"
                  />
                  <span>{task.title}</span>
                </label>
                <button
                  aria-label={`Delete ${task.title}`}
                  className="delete-button"
                  onClick={() => deleteTask(task)}
                  type="button"
                >
                  Delete
                </button>
              </li>
            ))}
          </ul>
        )}

        <p className="footer-note">Built to learn by doing.</p>
      </section>
    </main>
  );
}
