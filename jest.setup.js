// Mock codegenNativeComponent for all native components
jest.mock("react-native/Libraries/Utilities/codegenNativeComponent", () => {
  const React = require("react");
  return {
    __esModule: true,
    default: (name) => {
      const MockComponent = React.forwardRef((props, ref) => {
        return React.createElement("mock-" + name, { ...props, ref });
      });
      MockComponent.displayName = `Mock${name}`;
      return MockComponent;
    },
  };
});
