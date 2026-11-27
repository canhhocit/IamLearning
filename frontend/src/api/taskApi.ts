import type { Task } from "../types";

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(path, options);
  if (!response.ok) {
    throw new Error(`Request failed (${response.status})`);
  }
  return (await response.json()) as T;
}

export const taskApi = {
  getAll: () => request<Task[]>("/api/tasks"),
  create: (title: string) =>
    request<Task>("/api/tasks", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title }),
    }),
  updateCompletion: (id: string, completed: boolean) =>
    request<Task>(`/api/tasks/${id}`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ completed }),
    }),
  remove: async (id: string): Promise<void> => {
    const response = await fetch(`/api/tasks/${id}`, {
      method: "DELETE",
    });
    if (!response.ok) {
      throw new Error(`Request failed (${response.status})`);
    }
  },
};
