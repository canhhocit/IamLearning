import { defineConfig, devices } from "@playwright/test";

const reuseExistingServer = !process.env.CI;

export default defineConfig({
  testDir: "./tests",
  fullyParallel: true,
  reporter: "list",
  use: {
    baseURL: "http://127.0.0.1:5173",
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
      url: "http://127.0.0.1:8080/actuator/health",
      reuseExistingServer,
      timeout: 120_000,
    },
    {
      command: "npm run dev -- --host 127.0.0.1",
      cwd: "../frontend",
      url: "http://127.0.0.1:5173",
      reuseExistingServer,
      timeout: 30_000,
    },
  ],
});
