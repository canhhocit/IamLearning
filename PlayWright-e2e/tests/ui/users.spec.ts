import { expect, test } from "@playwright/test";

test.describe("Users UI", () => {
  test("creates, updates, and deletes a user from the user form", async ({
    page,
  }) => {
    const userName = `Learner ${Date.now()}`;
    const userEmail = `learner-${Date.now()}@example.com`;
    await page.goto("/");

    await page.getByLabel("Name", { exact: true }).fill(userName);
    await page.getByLabel("Email", { exact: true }).fill(userEmail);
    await page.getByRole("button", { name: "Create user" }).click();

    const creationNotice = page.locator(".user-feedback[role='status']");
    await expect(creationNotice).toContainText(userName);
    await expect(creationNotice).toContainText(userEmail);

    const usersTable = page.getByRole("table", { name: "Registered users" });
    const createdUserRow = usersTable
      .getByRole("row")
      .filter({ hasText: userEmail });
    await expect(createdUserRow).toContainText(userName);

    await page.reload();
    const reloadedTable = page.getByRole("table", { name: "Registered users" });
    const reloadedUserRow = reloadedTable
      .getByRole("row")
      .filter({ hasText: userEmail });
    await expect(reloadedUserRow).toContainText(userName);

    const updatedName = `${userName} Updated`;
    const updatedEmail = `updated-${userEmail}`;
    await reloadedUserRow.getByRole("button", { name: "Edit" }).click();

    const editForm = reloadedTable.locator(".user-edit-form");
    await editForm.getByLabel("Name", { exact: true }).fill(updatedName);
    await editForm.getByLabel("Email", { exact: true }).fill(updatedEmail);
    await editForm.getByRole("button", { name: "Save changes" }).click();

    const updatedUserRow = reloadedTable
      .getByRole("row")
      .filter({ hasText: updatedEmail });
    await expect(updatedUserRow).toContainText(updatedName);
    await expect(page.locator(".user-action-notice")).toHaveText(
      "User updated.",
    );

    await page.reload();
    const persistedUserRow = page
      .getByRole("table", { name: "Registered users" })
      .getByRole("row")
      .filter({ hasText: updatedEmail });
    await expect(persistedUserRow).toContainText(updatedName);

    await persistedUserRow
      .getByRole("button", { name: "Delete", exact: true })
      .click();
    const deleteConfirmation = persistedUserRow.getByRole("group", {
      name: `Confirm deleting ${updatedName}`,
    });
    await expect(deleteConfirmation).toBeVisible();
    await deleteConfirmation.getByRole("button", { name: "Cancel" }).click();
    await expect(persistedUserRow).toBeVisible();

    await persistedUserRow
      .getByRole("button", { name: "Delete", exact: true })
      .click();
    await persistedUserRow
      .getByRole("button", { name: "Confirm delete" })
      .click();
    await expect(persistedUserRow).toHaveCount(0);
    await expect(page.locator(".user-action-notice")).toHaveText(
      "User deleted.",
    );

    await page.reload();
    await expect(
      page.locator(".user-table tbody tr").filter({ hasText: updatedEmail }),
    ).toHaveCount(0);
  });
});
