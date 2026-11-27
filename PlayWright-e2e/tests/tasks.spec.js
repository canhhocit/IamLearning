import { expect, test } from "@playwright/test";

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
