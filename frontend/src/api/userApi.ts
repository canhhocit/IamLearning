import type { User } from "../types";

async function throwRequestError(response: Response): Promise<never> {
  const errorBody: unknown = await response.json().catch(() => null);
  const message =
    typeof errorBody === "object" &&
    errorBody !== null &&
    "message" in errorBody &&
    typeof errorBody.message === "string"
      ? errorBody.message
      : `User request failed (HTTP ${response.status}).`;
  throw new Error(message);
}

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(path, options);
  if (!response.ok) {
    await throwRequestError(response);
  }
  return (await response.json()) as T;
}

async function removeUser(id: string): Promise<void> {
  const response = await fetch(`/api/users/${id}`, { method: "DELETE" });
  if (!response.ok) {
    await throwRequestError(response);
  }
}

export const userApi = {
  getAll: () => request<User[]>("/api/users"),
  create: (name: string, email: string) =>
    request<User>("/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email }),
    }),
  update: (user: User) =>
    request<User>(`/api/users/${user.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: user.name, email: user.email }),
    }),
  remove: removeUser,
};
