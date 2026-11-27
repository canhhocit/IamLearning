import { expect, test } from "@playwright/test";

type ApiErrorResponse = {
  error: string;
  message: string;
  validationErrors: Record<string, string>;
};

test("creates, completes, and deletes a task", async ({ page }) => {
  const taskTitle = `Learn Playwright ${Date.now()}`;
  await page.goto("/");

  await page.getByRole("textbox", { name: "New task" }).fill(taskTitle);
  await page.getByRole("button", { name: "Add task" }).click();

  const taskItem = page.getByRole("listitem").filter({ hasText: taskTitle });
  await expect(taskItem).toBeVisible();
  const checkbox = taskItem.getByRole("checkbox");
  await checkbox.click();
  await expect(checkbox).toBeChecked();

  await page.getByRole("button", { name: `Delete ${taskTitle}` }).click();
  await expect(taskItem).toHaveCount(0);
});

test("creates a user from the user form", async ({ page }) => {
  const userName = `Learner ${Date.now()}`;
  const userEmail = `learner-${Date.now()}@example.com`;
  await page.goto("/");

  await page.getByLabel("Name", { exact: true }).fill(userName);
  await page.getByLabel("Email", { exact: true }).fill(userEmail);
  await page.getByRole("button", { name: "Create user" }).click();

  await expect(page.getByRole("status")).toContainText(userName);
  await expect(page.getByRole("status")).toContainText(userEmail);
});

test("returns a consistent error response for invalid requests", async ({ request }) => {
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
    message: "Task with id '00000000-0000-0000-0000-000000000000' was not found",
  });
});
