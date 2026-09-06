import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import type { NativeSyntheticEvent } from "react-native";

import { TextField } from "./TextField";
import type { TextChangeEvent } from "./TextFieldNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

describe("TextField", () => {
  describe("minHeight calculation", () => {
    it("base height is 64", async () => {
      const { getByTestId } = await render(<TextField testID="field" />);
      const field = getByTestId("field");
      expect(field.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }]));
    });

    it("adds 20 for helperText", async () => {
      const { getByTestId } = await render(<TextField testID="field" helperText="Some help" />);
      const field = getByTestId("field");
      expect(field.props.style).toEqual(expect.arrayContaining([{ minHeight: 84 }]));
    });

    it("adds 48 for multiline", async () => {
      const { getByTestId } = await render(<TextField testID="field" multiline />);
      const field = getByTestId("field");
      expect(field.props.style).toEqual(expect.arrayContaining([{ minHeight: 112 }]));
    });

    it("adds both for multiline with helperText", async () => {
      const { getByTestId } = await render(<TextField testID="field" multiline helperText="Help" />);
      const field = getByTestId("field");
      expect(field.props.style).toEqual(expect.arrayContaining([{ minHeight: 132 }]));
    });
  });

  describe("event handlers", () => {
    it("onChange extracts text from native event", async () => {
      const onChange = jest.fn();
      const { getByTestId } = await render(<TextField testID="field" onChange={onChange} />);
      const field = getByTestId("field");

      const event = createNativeEvent<TextChangeEvent>({ text: "hello" });
      field.props.onTextFieldChange(event);

      expect(onChange).toHaveBeenCalledTimes(1);
      expect(onChange).toHaveBeenCalledWith("hello");
    });

    it("onFocus is called without arguments", async () => {
      const onFocus = jest.fn();
      const { getByTestId } = await render(<TextField testID="field" onFocus={onFocus} />);
      const field = getByTestId("field");

      field.props.onTextFieldFocus();

      expect(onFocus).toHaveBeenCalledTimes(1);
      expect(onFocus).toHaveBeenCalledWith();
    });

    it("onBlur is called without arguments", async () => {
      const onBlur = jest.fn();
      const { getByTestId } = await render(<TextField testID="field" onBlur={onBlur} />);
      const field = getByTestId("field");

      field.props.onTextFieldBlur();

      expect(onBlur).toHaveBeenCalledTimes(1);
      expect(onBlur).toHaveBeenCalledWith();
    });
  });

  describe("default props", () => {
    it("disabled defaults to false", async () => {
      const { getByTestId } = await render(<TextField testID="field" />);
      const field = getByTestId("field");
      expect(field.props.disabled).toBe(false);
    });

    it("editable defaults to true", async () => {
      const { getByTestId } = await render(<TextField testID="field" />);
      const field = getByTestId("field");
      expect(field.props.editable).toBe(true);
    });

    it("multiline defaults to false", async () => {
      const { getByTestId } = await render(<TextField testID="field" />);
      const field = getByTestId("field");
      expect(field.props.multiline).toBe(false);
    });

    it("secureTextEntry defaults to false", async () => {
      const { getByTestId } = await render(<TextField testID="field" />);
      const field = getByTestId("field");
      expect(field.props.secureTextEntry).toBe(false);
    });

    it("error defaults to false", async () => {
      const { getByTestId } = await render(<TextField testID="field" />);
      const field = getByTestId("field");
      expect(field.props.error).toBe(false);
    });
  });

  describe("prop passthrough", () => {
    it("passes value prop", async () => {
      const { getByTestId } = await render(<TextField testID="field" value="test value" />);
      const field = getByTestId("field");
      expect(field.props.value).toBe("test value");
    });

    it("passes label prop", async () => {
      const { getByTestId } = await render(<TextField testID="field" label="Name" />);
      const field = getByTestId("field");
      expect(field.props.label).toBe("Name");
    });

    it("passes placeholder prop", async () => {
      const { getByTestId } = await render(<TextField testID="field" placeholder="Enter name" />);
      const field = getByTestId("field");
      expect(field.props.placeholder).toBe("Enter name");
    });

    it("passes maxLength prop", async () => {
      const { getByTestId } = await render(<TextField testID="field" maxLength={100} />);
      const field = getByTestId("field");
      expect(field.props.maxLength).toBe(100);
    });

    it("passes helperText prop", async () => {
      const { getByTestId } = await render(<TextField testID="field" helperText="Required field" />);
      const field = getByTestId("field");
      expect(field.props.helperText).toBe("Required field");
    });

    it("passes disabled as true when set", async () => {
      const { getByTestId } = await render(<TextField testID="field" disabled />);
      const field = getByTestId("field");
      expect(field.props.disabled).toBe(true);
    });

    it("passes editable as false when set", async () => {
      const { getByTestId } = await render(<TextField testID="field" editable={false} />);
      const field = getByTestId("field");
      expect(field.props.editable).toBe(false);
    });

    it("passes multiline as true when set", async () => {
      const { getByTestId } = await render(<TextField testID="field" multiline />);
      const field = getByTestId("field");
      expect(field.props.multiline).toBe(true);
    });

    it("passes secureTextEntry as true when set", async () => {
      const { getByTestId } = await render(<TextField testID="field" secureTextEntry />);
      const field = getByTestId("field");
      expect(field.props.secureTextEntry).toBe(true);
    });

    it("passes error as true when set", async () => {
      const { getByTestId } = await render(<TextField testID="field" error />);
      const field = getByTestId("field");
      expect(field.props.error).toBe(true);
    });
  });

  describe("style handling", () => {
    it("merges custom style with computed minHeight", async () => {
      const customStyle = { backgroundColor: "blue" };
      const { getByTestId } = await render(<TextField testID="field" style={customStyle} />);
      const field = getByTestId("field");
      expect(field.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }, customStyle]));
    });
  });
});
