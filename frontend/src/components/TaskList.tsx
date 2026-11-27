import TaskItem from "./TaskItem";
import type { Task } from "../types";

interface TaskListProps {
  tasks: Task[];
  onToggleTask: (task: Task) => void;
  onDeleteTask: (task: Task) => void;
}

export default function TaskList({
  tasks,
  onToggleTask,
  onDeleteTask,
}: TaskListProps) {
  if (tasks.length === 0) {
    return <p className="empty-state">No tasks yet. Add one to get started.</p>;
  }

  return (
    <ul className="task-list">
      {tasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onToggle={onToggleTask}
          onDelete={onDeleteTask}
        />
      ))}
    </ul>
  );
}
