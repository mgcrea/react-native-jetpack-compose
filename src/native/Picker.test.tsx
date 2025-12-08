import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import React from "react";
import type { NativeSyntheticEvent } from "react-native";

import { Picker } from "./Picker";
import type { PickerChangeEvent } from "./PickerNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

const sampleOptions = [
  { value: "us", label: "United States" },
  { value: "uk", label: "United Kingdom" },
  { value: "fr", label: "France" },
];

describe("Picker", () => {
  describe("prop mapping", () => {
    it("maps options to items", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.items).toEqual(sampleOptions);
    });

    it("maps value to selectedValue", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} value="uk" />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedValue).toBe("uk");
    });

    it("handles null value", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} value={null} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedValue).toBeNull();
    });

    it("handles undefined value", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedValue).toBeUndefined();
    });
  });

  describe("event handlers", () => {
    it("onChange extracts value from native event", () => {
      const onChange = jest.fn();
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} onChange={onChange} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<PickerChangeEvent>({ value: "fr", index: 2 });
      picker.props.onValueChange(event);

      expect(onChange).toHaveBeenCalledTimes(1);
      expect(onChange).toHaveBeenCalledWith("fr");
    });

    it("does not throw when onChange is not provided", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<PickerChangeEvent>({ value: "fr", index: 2 });
      expect(() => picker.props.onValueChange(event)).not.toThrow();
    });
  });

  describe("default props", () => {
    it("disabled defaults to false", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.disabled).toBe(false);
    });
  });

  describe("prop passthrough", () => {
    it("passes label prop", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} label="Country" />);
      const picker = getByTestId("picker");
      expect(picker.props.label).toBe("Country");
    });

    it("passes placeholder prop", () => {
      const { getByTestId } = render(
        <Picker testID="picker" options={sampleOptions} placeholder="Select a country" />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.placeholder).toBe("Select a country");
    });

    it("passes disabled as true when set", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} disabled />);
      const picker = getByTestId("picker");
      expect(picker.props.disabled).toBe(true);
    });
  });

  describe("style handling", () => {
    it("applies base minHeight style", () => {
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }]));
    });

    it("merges custom style with base style", () => {
      const customStyle = { backgroundColor: "green" };
      const { getByTestId } = render(<Picker testID="picker" options={sampleOptions} style={customStyle} />);
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }, customStyle]));
    });
  });
});
