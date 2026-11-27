async function request(path, options) {
  const response = await fetch(path, options);
  if (!response.ok) {
    throw new Error(`Request failed (${response.status})`);
  }
  if (response.status === 204) {
    return null;
  }
  return response.json();
}

export const tasksApi = {
  list: () => request("/api/tasks"),
  create: (title) =>
    request("/api/tasks", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title }),
    }),
  setCompleted: (id, completed) =>
    request(`/api/tasks/${id}`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ completed }),
    }),
  remove: (id) =>
    request(`/api/tasks/${id}`, {
      method: "DELETE",
    }),
};
