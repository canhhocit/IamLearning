import { expect, test } from "@playwright/test";

test.describe("Tasks UI", () => {
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

  test("creates task via API, completes it on UI, then cleans up via API", async ({
    page,
    request,
  }) => {
    const taskTitle = `Hybrid Test ${Date.now()}`;

    const createRes = await request.post("/api/tasks", {
      data: { title: taskTitle },
    });
    expect(createRes.ok()).toBeTruthy();
    const createdTask: { id: string; title: string; completed: boolean } =
      await createRes.json();

    try {
      await page.goto("/");
      const taskItem = page.getByRole("listitem").filter({ hasText: taskTitle });
      await expect(taskItem).toBeVisible();

      const checkbox = taskItem.getByRole("checkbox");
      await checkbox.click();
      await expect(checkbox).toBeChecked();
    } finally {
      const deleteRes = await request.delete(`/api/tasks/${createdTask.id}`);
      expect(deleteRes.ok()).toBeTruthy();
    }
  });

  test("shows error message when creating task fails with 500", async ({
    page,
  }) => {
    await page.route("**/api/tasks", async (route) => {
      if (route.request().method() === "POST") {
        await route.fulfill({ status: 500, body: "Internal Server Error" });
        return;
      }
      await route.continue();
    });

    await page.goto("/");
    await page.getByRole("textbox", { name: "New task" }).fill("Task will fail");
    await page.getByRole("button", { name: "Add task" }).click();

    await expect(
      page.getByText("Could not add the task. Please try again."),
    ).toBeVisible();
  });
});
