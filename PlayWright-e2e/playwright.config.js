import { defineConfig, devices } from "@playwright/test";

const reuseExistingServer = !process.env.CI;
const backendBaseUrl = process.env.BACKEND_BASE_URL ?? "http://127.0.0.1:8080";
const frontendBaseUrl = process.env.PLAYWRIGHT_BASE_URL ?? "http://127.0.0.1:5173";

export default defineConfig({
  testDir: "./tests",
  fullyParallel: true,
  reporter: "list",
  use: {
    baseURL: frontendBaseUrl,
    trace: "on-first-retry",
  },
  projects: [
    {
      name: "chromium",
      use: { ...devices["Desktop Chrome"] },
    },
  ],
  webServer: [
    {
      command: "mvn spring-boot:run",
      cwd: "../backend",
      url: `${backendBaseUrl}/actuator/health`,
      reuseExistingServer,
      timeout: 120_000,
    },
    {
      command: "npm run dev -- --host 127.0.0.1",
      cwd: "../frontend",
      url: frontendBaseUrl,
      reuseExistingServer,
      timeout: 30_000,
    },
  ],
});
