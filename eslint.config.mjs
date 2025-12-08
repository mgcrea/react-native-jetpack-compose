import baseConfig from "@mgcrea/eslint-config-react-native";

const config = [
  ...baseConfig,
  {
    rules: {
      "react/prop-types": "off",
    },
  },
  {
    ignores: [".idea/**", "example/**", "test/**", "docs/**", "jest.setup.js"],
  },
  {
    languageOptions: {
      parserOptions: {
        project: ["./tsconfig.node.json", "./tsconfig.json"],
        tsconfigRootDir: import.meta.dirname,
      },
    },
  },
  {
    files: ["**/*.test.{ts,tsx}"],
    rules: {
      "testing-library/prefer-screen-queries": "off",
      "@typescript-eslint/no-unsafe-member-access": "off",
      "@typescript-eslint/no-unsafe-return": "off",
    },
  },
];

export default config;
