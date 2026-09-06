import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import type { NativeSyntheticEvent } from "react-native";

import { TimePicker } from "./TimePicker";
import type { CancelEvent, ConfirmEvent, TimeChangeEvent } from "./TimePickerNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

describe("TimePicker", () => {
  describe("toMinutesFromMidnight conversion (via value prop)", () => {
    it("handles undefined value", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" value={undefined} />);
      expect(getByTestId("picker").props.selectedTimeMinutes).toBeUndefined();
    });

    it("handles null value", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" value={null} />);
      expect(getByTestId("picker").props.selectedTimeMinutes).toBeUndefined();
    });

    it("converts a Date to minutes from midnight", async () => {
      const date = new Date(2024, 0, 1, 14, 30);
      const { getByTestId } = await render(<TimePicker testID="picker" value={date} />);
      expect(getByTestId("picker").props.selectedTimeMinutes).toBe(870);
    });

    it("converts midnight to 0", async () => {
      const date = new Date(2024, 0, 1, 0, 0);
      const { getByTestId } = await render(<TimePicker testID="picker" value={date} />);
      expect(getByTestId("picker").props.selectedTimeMinutes).toBe(0);
    });
  });

  describe("event handlers", () => {
    it("onConfirm receives a Date with the selected hours and minutes", async () => {
      const onConfirm = jest.fn<(time: Date | null) => void>();
      const { getByTestId } = await render(<TimePicker testID="picker" onConfirm={onConfirm} />);

      getByTestId("picker").props.onConfirm(createNativeEvent<ConfirmEvent>({ selectedTimeMinutes: 870 }));

      expect(onConfirm).toHaveBeenCalledTimes(1);
      const [time] = onConfirm.mock.calls[0];
      expect(time).toBeInstanceOf(Date);
      expect(time?.getHours()).toBe(14);
      expect(time?.getMinutes()).toBe(30);
      expect(time?.getSeconds()).toBe(0);
    });

    it("onConfirm receives midnight for 0 minutes", async () => {
      const onConfirm = jest.fn<(time: Date | null) => void>();
      const { getByTestId } = await render(<TimePicker testID="picker" onConfirm={onConfirm} />);

      getByTestId("picker").props.onConfirm(createNativeEvent<ConfirmEvent>({ selectedTimeMinutes: 0 }));

      const [time] = onConfirm.mock.calls[0];
      expect(time?.getHours()).toBe(0);
      expect(time?.getMinutes()).toBe(0);
    });

    it("onCancel is called without arguments", async () => {
      const onCancel = jest.fn();
      const { getByTestId } = await render(<TimePicker testID="picker" onCancel={onCancel} />);

      getByTestId("picker").props.onCancel(createNativeEvent<CancelEvent>({}));

      expect(onCancel).toHaveBeenCalledTimes(1);
      expect(onCancel).toHaveBeenCalledWith();
    });

    it("onChange receives a Date with the selected hours and minutes", async () => {
      const onChange = jest.fn<(time: Date | null) => void>();
      const { getByTestId } = await render(<TimePicker testID="picker" onChange={onChange} />);

      getByTestId("picker").props.onTimeChange(
        createNativeEvent<TimeChangeEvent>({ selectedTimeMinutes: 65 }),
      );

      const [time] = onChange.mock.calls[0];
      expect(time?.getHours()).toBe(1);
      expect(time?.getMinutes()).toBe(5);
    });
  });

  describe("default props", () => {
    it("initialDisplayMode defaults to picker", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" />);
      expect(getByTestId("picker").props.initialDisplayMode).toBe("picker");
    });

    it("showModeToggle defaults to true", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" />);
      expect(getByTestId("picker").props.showModeToggle).toBe(true);
    });

    it("is24Hour is undefined by default", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" />);
      expect(getByTestId("picker").props.is24Hour).toBeUndefined();
    });
  });

  describe("prop passthrough", () => {
    it("passes title as titleText", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" title="Select Time" />);
      expect(getByTestId("picker").props.titleText).toBe("Select Time");
    });

    it("passes is24Hour prop", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" is24Hour />);
      expect(getByTestId("picker").props.is24Hour).toBe(true);
    });

    it("passes label, placeholder and disabled props", async () => {
      const { getByTestId } = await render(
        <TimePicker testID="picker" label="Meeting" placeholder="Pick a time" disabled />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.label).toBe("Meeting");
      expect(picker.props.placeholder).toBe("Pick a time");
      expect(picker.props.disabled).toBe(true);
    });

    it("passes confirmLabel and cancelLabel props", async () => {
      const { getByTestId } = await render(
        <TimePicker testID="picker" confirmLabel="Done" cancelLabel="Dismiss" />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.confirmLabel).toBe("Done");
      expect(picker.props.cancelLabel).toBe("Dismiss");
    });
  });

  describe("style handling", () => {
    it("applies base minHeight style", async () => {
      const { getByTestId } = await render(<TimePicker testID="picker" />);
      expect(getByTestId("picker").props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }]));
    });

    it("merges custom style with base style", async () => {
      const customStyle = { backgroundColor: "red" };
      const { getByTestId } = await render(<TimePicker testID="picker" style={customStyle} />);
      expect(getByTestId("picker").props.style).toEqual(
        expect.arrayContaining([{ minHeight: 64 }, customStyle]),
      );
    });
  });
});
