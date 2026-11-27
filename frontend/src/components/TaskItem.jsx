export default function TaskItem({ task, onToggle, onDelete }) {
  return (
    <li className="task-item">
      <label className={task.completed ? "task-title completed" : "task-title"}>
        <input
          checked={task.completed}
          onChange={() => onToggle(task)}
          type="checkbox"
        />
        <span>{task.title}</span>
      </label>
      <button
        aria-label={`Delete ${task.title}`}
        className="delete-button"
        onClick={() => onDelete(task)}
        type="button"
      >
        Delete
      </button>
    </li>
  );
}
