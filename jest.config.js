/** @type {import('jest').Config} */
module.exports = {
  preset: "react-native",
  testMatch: ["<rootDir>/src/**/*.test.{ts,tsx}"],
  setupFilesAfterEnv: ["<rootDir>/jest.setup.js"],
  moduleFileExtensions: ["ts", "tsx", "js", "jsx", "json"],
  transformIgnorePatterns: [
    "node_modules/(?!(.pnpm/[^/]+/node_modules/)?(react-native|@react-native|@testing-library)/)",
  ],
};
