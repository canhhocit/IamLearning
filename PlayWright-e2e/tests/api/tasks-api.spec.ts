import { expect, test } from "@playwright/test";

type ApiErrorResponse = {
  error: string;
  message: string;
  validationErrors: Record<string, string>;
};

test.describe("Tasks API", () => {
  test("returns a consistent error response for invalid requests", async ({
    request,
  }) => {
    const invalidTaskResponse = await request.post("/api/tasks", {
      data: { title: " " },
    });
    expect(invalidTaskResponse.status()).toBe(400);
    const validationError: ApiErrorResponse = await invalidTaskResponse.json();
    expect(validationError).toMatchObject({
      error: "Bad Request",
      message: "Request validation failed",
    });
    expect(validationError.validationErrors.title).toBeTruthy();

    const missingTaskResponse = await request.patch(
      "/api/tasks/00000000-0000-0000-0000-000000000000",
      { data: { completed: true } },
    );
    expect(missingTaskResponse.status()).toBe(404);
    const notFoundError: ApiErrorResponse = await missingTaskResponse.json();
    expect(notFoundError).toMatchObject({
      error: "Not Found",
      message:
        "Task with id '00000000-0000-0000-0000-000000000000' was not found",
    });
  });
});
