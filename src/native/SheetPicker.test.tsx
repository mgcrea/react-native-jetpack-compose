import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import React from "react";
import type { NativeSyntheticEvent } from "react-native";

import { SheetPicker } from "./SheetPicker";
import type { SheetPickerDismissEvent, SheetPickerSelectEvent } from "./SheetPickerNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

const sampleOptions = [
  { value: "us", label: "United States" },
  { value: "uk", label: "United Kingdom" },
  { value: "fr", label: "France" },
];

describe("SheetPicker", () => {
  describe("prop mapping", () => {
    it("passes options directly", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.options).toEqual(sampleOptions);
    });

    it("maps value to selectedValue", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} value="uk" />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedValue).toBe("uk");
    });

    it("handles null value", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} value={null} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedValue).toBeNull();
    });

    it("maps onSelect to onItemSelect", () => {
      const onSelect = jest.fn();
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} onSelect={onSelect} />,
      );
      const picker = getByTestId("picker");

      const event = createNativeEvent<SheetPickerSelectEvent>({ value: "fr" });
      picker.props.onItemSelect(event);

      expect(onSelect).toHaveBeenCalledWith("fr");
    });
  });

  describe("event handlers", () => {
    it("onSelect extracts value from native event", () => {
      const onSelect = jest.fn();
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} onSelect={onSelect} />,
      );
      const picker = getByTestId("picker");

      const event = createNativeEvent<SheetPickerSelectEvent>({ value: "us" });
      picker.props.onItemSelect(event);

      expect(onSelect).toHaveBeenCalledTimes(1);
      expect(onSelect).toHaveBeenCalledWith("us");
    });

    it("onDismiss is called without arguments", () => {
      const onDismiss = jest.fn();
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} onDismiss={onDismiss} />,
      );
      const picker = getByTestId("picker");

      const event = createNativeEvent<SheetPickerDismissEvent>({});
      picker.props.onDismiss(event);

      expect(onDismiss).toHaveBeenCalledTimes(1);
      expect(onDismiss).toHaveBeenCalledWith();
    });
  });

  describe("default props", () => {
    it("autoDismiss defaults to true", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.autoDismiss).toBe(true);
    });

    it("maxHeightRatio defaults to 0.9", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.maxHeightRatio).toBe(0.9);
    });

    it("disabled defaults to false", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.disabled).toBe(false);
    });
  });

  describe("prop passthrough", () => {
    it("passes title prop", () => {
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} title="Select Country" />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.title).toBe("Select Country");
    });

    it("passes searchPlaceholder prop", () => {
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} searchPlaceholder="Search..." />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.searchPlaceholder).toBe("Search...");
    });

    it("passes label prop", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} label="Country" />);
      const picker = getByTestId("picker");
      expect(picker.props.label).toBe("Country");
    });

    it("passes placeholder prop", () => {
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} placeholder="Select a country" />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.placeholder).toBe("Select a country");
    });

    it("passes sheetMaxWidth prop", () => {
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} sheetMaxWidth={400} />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.sheetMaxWidth).toBe(400);
    });

    it("passes autoDismiss as false when set", () => {
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} autoDismiss={false} />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.autoDismiss).toBe(false);
    });

    it("passes custom maxHeightRatio", () => {
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} maxHeightRatio={0.5} />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.maxHeightRatio).toBe(0.5);
    });

    it("passes disabled as true when set", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} disabled />);
      const picker = getByTestId("picker");
      expect(picker.props.disabled).toBe(true);
    });
  });

  describe("style handling", () => {
    it("applies base minHeight style", () => {
      const { getByTestId } = render(<SheetPicker testID="picker" options={sampleOptions} />);
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }]));
    });

    it("merges custom style with base style", () => {
      const customStyle = { backgroundColor: "yellow" };
      const { getByTestId } = render(
        <SheetPicker testID="picker" options={sampleOptions} style={customStyle} />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }, customStyle]));
    });
  });
});
