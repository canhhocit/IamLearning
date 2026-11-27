import { useState, type FormEvent } from "react";

interface TaskFormProps {
  onAddTask: (title: string) => Promise<boolean>;
}

export default function TaskForm({ onAddTask }: TaskFormProps) {
  const [title, setTitle] = useState("");

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const trimmedTitle = title.trim();
    if (!trimmedTitle) return;

    const created = await onAddTask(trimmedTitle);
    if (created) {
      setTitle("");
    }
  }

  return (
    <form className="task-form" onSubmit={handleSubmit}>
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
  );
}
