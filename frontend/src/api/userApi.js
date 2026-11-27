export const userApi = {
  async create(name, email) {
    const response = await fetch("/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email }),
    });

    if (!response.ok) {
      const errorBody = await response.json().catch(() => null);
      throw new Error(
        errorBody?.message ?? `Could not create user (HTTP ${response.status}).`,
      );
    }

    return response.json();
  },
};
