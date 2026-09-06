import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import type { NativeSyntheticEvent } from "react-native";

import { SegmentedButton } from "./SegmentedButton";
import type {
  SegmentedButtonChangeEvent,
  SegmentedButtonMultiChangeEvent,
} from "./SegmentedButtonNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

const sampleOptions = [
  { value: "s", label: "Small" },
  { value: "m", label: "Medium" },
  { value: "l", label: "Large" },
];

const optionsWithIcons = [
  { value: "list", label: "List", icon: "List" },
  { value: "grid", label: "Grid", icon: "Grid" },
];

describe("SegmentedButton", () => {
  describe("single-select mode (default)", () => {
    it("maps options to segments", async () => {
      const { getByTestId } = await render(<SegmentedButton testID="segmented" options={sampleOptions} />);
      const component = getByTestId("segmented");
      expect(component.props.segments).toEqual(sampleOptions);
    });

    it("maps value to selectedValue", async () => {
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} value="m" />,
      );
      const component = getByTestId("segmented");
      expect(component.props.selectedValue).toBe("m");
    });

    it("sets multiSelect to false by default", async () => {
      const { getByTestId } = await render(<SegmentedButton testID="segmented" options={sampleOptions} />);
      const component = getByTestId("segmented");
      expect(component.props.multiSelect).toBe(false);
    });

    it("handles null value", async () => {
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} value={null} />,
      );
      const component = getByTestId("segmented");
      expect(component.props.selectedValue).toBeNull();
    });

    it("handles undefined value", async () => {
      const { getByTestId } = await render(<SegmentedButton testID="segmented" options={sampleOptions} />);
      const component = getByTestId("segmented");
      expect(component.props.selectedValue).toBeUndefined();
    });

    it("onChange extracts value from native event", async () => {
      const onChange = jest.fn();
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} onChange={onChange} />,
      );
      const component = getByTestId("segmented");

      const event = createNativeEvent<SegmentedButtonChangeEvent>({ value: "l", index: 2 });
      component.props.onValueChange(event);

      expect(onChange).toHaveBeenCalledTimes(1);
      expect(onChange).toHaveBeenCalledWith("l");
    });

    it("does not throw when onChange is not provided", async () => {
      const { getByTestId } = await render(<SegmentedButton testID="segmented" options={sampleOptions} />);
      const component = getByTestId("segmented");

      const event = createNativeEvent<SegmentedButtonChangeEvent>({ value: "l", index: 2 });
      expect(() => component.props.onValueChange(event)).not.toThrow();
    });
  });

  describe("multi-select mode", () => {
    it("sets multiSelect to true", async () => {
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} multiSelect value={[]} />,
      );
      const component = getByTestId("segmented");
      expect(component.props.multiSelect).toBe(true);
    });

    it("maps value array to selectedValues", async () => {
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} multiSelect value={["s", "l"]} />,
      );
      const component = getByTestId("segmented");
      expect(component.props.selectedValues).toEqual(["s", "l"]);
    });

    it("selectedValue is undefined in multi-select mode", async () => {
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} multiSelect value={["s"]} />,
      );
      const component = getByTestId("segmented");
      expect(component.props.selectedValue).toBeUndefined();
    });

    it("onChange extracts values array from native event", async () => {
      const onChange = jest.fn();
      const { getByTestId } = await render(
        <SegmentedButton
          testID="segmented"
          options={sampleOptions}
          multiSelect
          value={[]}
          onChange={onChange}
        />,
      );
      const component = getByTestId("segmented");

      const event = createNativeEvent<SegmentedButtonMultiChangeEvent>({
        values: ["s", "m"],
      });
      component.props.onValuesChange(event);

      expect(onChange).toHaveBeenCalledTimes(1);
      expect(onChange).toHaveBeenCalledWith(["s", "m"]);
    });

    it("does not throw when onChange is not provided in multi-select", async () => {
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} multiSelect value={[]} />,
      );
      const component = getByTestId("segmented");

      const event = createNativeEvent<SegmentedButtonMultiChangeEvent>({
        values: ["s"],
      });
      expect(() => component.props.onValuesChange(event)).not.toThrow();
    });
  });

  describe("with icons", () => {
    it("passes icon property in segments", async () => {
      const { getByTestId } = await render(<SegmentedButton testID="segmented" options={optionsWithIcons} />);
      const component = getByTestId("segmented");
      expect(component.props.segments[0].icon).toBe("List");
      expect(component.props.segments[1].icon).toBe("Grid");
    });
  });

  describe("disabled state", () => {
    it("disabled defaults to false", async () => {
      const { getByTestId } = await render(<SegmentedButton testID="segmented" options={sampleOptions} />);
      const component = getByTestId("segmented");
      expect(component.props.disabled).toBe(false);
    });

    it("passes disabled as true when set", async () => {
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} disabled />,
      );
      const component = getByTestId("segmented");
      expect(component.props.disabled).toBe(true);
    });
  });

  describe("style handling", () => {
    it("applies base minHeight style", async () => {
      const { getByTestId } = await render(<SegmentedButton testID="segmented" options={sampleOptions} />);
      const component = getByTestId("segmented");
      expect(component.props.style).toEqual(expect.arrayContaining([{ minHeight: 48 }]));
    });

    it("merges custom style with base style", async () => {
      const customStyle = { marginTop: 16 };
      const { getByTestId } = await render(
        <SegmentedButton testID="segmented" options={sampleOptions} style={customStyle} />,
      );
      const component = getByTestId("segmented");
      expect(component.props.style).toEqual(expect.arrayContaining([{ minHeight: 48 }, customStyle]));
    });
  });
});
