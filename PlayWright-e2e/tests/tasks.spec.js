import { expect, test } from "@playwright/test";

test("creates, completes, and deletes a task", async ({ page }) => {
  const taskTitle = `Learn Playwright ${Date.now()}`;
  await page.goto("/");

  await page.getByRole("textbox", { name: "New task" }).fill(taskTitle);
  await page.getByRole("button", { name: "Add task" }).click();

  const task = page.getByText(taskTitle);
  await expect(task).toBeVisible();
  await page.getByRole("checkbox").check();
  await expect(page.getByText("1/1 done")).toBeVisible();

  await page.getByRole("button", { name: `Delete ${taskTitle}` }).click();
  await expect(task).toHaveCount(0);
});
