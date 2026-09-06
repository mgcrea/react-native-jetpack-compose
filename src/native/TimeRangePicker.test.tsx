import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import type { NativeSyntheticEvent } from "react-native";

import { TimeRangePicker, type TimeRange } from "./TimeRangePicker";
import type {
  RangeCancelEvent,
  RangeConfirmEvent,
  RangeTimeChangeEvent,
} from "./TimeRangePickerNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

describe("TimeRangePicker", () => {
  describe("toMinutesFromMidnight conversion (via value prop)", () => {
    it("handles undefined value", async () => {
      const { getByTestId } = await render(<TimeRangePicker testID="picker" value={undefined} />);
      const picker = getByTestId("picker");
      expect(picker.props.startTimeMinutes).toBeUndefined();
      expect(picker.props.endTimeMinutes).toBeUndefined();
    });

    it("handles null value", async () => {
      const { getByTestId } = await render(<TimeRangePicker testID="picker" value={null} />);
      const picker = getByTestId("picker");
      expect(picker.props.startTimeMinutes).toBeUndefined();
      expect(picker.props.endTimeMinutes).toBeUndefined();
    });

    it("converts both ends of the range to minutes", async () => {
      const value = {
        startTime: new Date(2024, 0, 1, 9, 0),
        endTime: new Date(2024, 0, 1, 17, 30),
      };
      const { getByTestId } = await render(<TimeRangePicker testID="picker" value={value} />);
      const picker = getByTestId("picker");
      expect(picker.props.startTimeMinutes).toBe(540);
      expect(picker.props.endTimeMinutes).toBe(1050);
    });

    it("handles a partial range", async () => {
      const value = { startTime: new Date(2024, 0, 1, 9, 0), endTime: null };
      const { getByTestId } = await render(<TimeRangePicker testID="picker" value={value} />);
      const picker = getByTestId("picker");
      expect(picker.props.startTimeMinutes).toBe(540);
      expect(picker.props.endTimeMinutes).toBeUndefined();
    });
  });

  describe("event handlers", () => {
    it("onConfirm receives a range of Dates", async () => {
      const onConfirm = jest.fn<(range: TimeRange) => void>();
      const { getByTestId } = await render(<TimeRangePicker testID="picker" onConfirm={onConfirm} />);

      getByTestId("picker").props.onConfirm(
        createNativeEvent<RangeConfirmEvent>({ startTimeMinutes: 540, endTimeMinutes: 1050 }),
      );

      expect(onConfirm).toHaveBeenCalledTimes(1);
      const [range] = onConfirm.mock.calls[0];
      expect(range.startTime?.getHours()).toBe(9);
      expect(range.startTime?.getMinutes()).toBe(0);
      expect(range.endTime?.getHours()).toBe(17);
      expect(range.endTime?.getMinutes()).toBe(30);
    });

    it("both ends of the range fall on the same calendar day", async () => {
      const onConfirm = jest.fn<(range: TimeRange) => void>();
      const { getByTestId } = await render(<TimeRangePicker testID="picker" onConfirm={onConfirm} />);

      getByTestId("picker").props.onConfirm(
        createNativeEvent<RangeConfirmEvent>({ startTimeMinutes: 0, endTimeMinutes: 1439 }),
      );

      const [range] = onConfirm.mock.calls[0];
      expect(range.startTime?.toDateString()).toBe(range.endTime?.toDateString());
    });

    it("onCancel is called without arguments", async () => {
      const onCancel = jest.fn();
      const { getByTestId } = await render(<TimeRangePicker testID="picker" onCancel={onCancel} />);

      getByTestId("picker").props.onCancel(createNativeEvent<RangeCancelEvent>({}));

      expect(onCancel).toHaveBeenCalledTimes(1);
      expect(onCancel).toHaveBeenCalledWith();
    });

    it("onChange receives null for an unset end", async () => {
      const onChange = jest.fn<(range: TimeRange) => void>();
      const { getByTestId } = await render(<TimeRangePicker testID="picker" onChange={onChange} />);

      getByTestId("picker").props.onTimeChange(
        createNativeEvent<RangeTimeChangeEvent>({ startTimeMinutes: 540, endTimeMinutes: undefined }),
      );

      const [range] = onChange.mock.calls[0];
      expect(range.startTime?.getHours()).toBe(9);
      expect(range.endTime).toBeNull();
    });
  });

  describe("default props", () => {
    it("initialDisplayMode defaults to picker", async () => {
      const { getByTestId } = await render(<TimeRangePicker testID="picker" />);
      expect(getByTestId("picker").props.initialDisplayMode).toBe("picker");
    });

    it("showModeToggle defaults to true", async () => {
      const { getByTestId } = await render(<TimeRangePicker testID="picker" />);
      expect(getByTestId("picker").props.showModeToggle).toBe(true);
    });
  });

  describe("prop passthrough", () => {
    it("passes title as titleText", async () => {
      const { getByTestId } = await render(<TimeRangePicker testID="picker" title="Working Hours" />);
      expect(getByTestId("picker").props.titleText).toBe("Working Hours");
    });

    it("passes label, placeholder and disabled props", async () => {
      const { getByTestId } = await render(
        <TimeRangePicker testID="picker" label="Hours" placeholder="Pick a range" disabled />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.label).toBe("Hours");
      expect(picker.props.placeholder).toBe("Pick a range");
      expect(picker.props.disabled).toBe(true);
    });
  });

  describe("style handling", () => {
    it("applies base minHeight style", async () => {
      const { getByTestId } = await render(<TimeRangePicker testID="picker" />);
      expect(getByTestId("picker").props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }]));
    });

    it("merges custom style with base style", async () => {
      const customStyle = { backgroundColor: "red" };
      const { getByTestId } = await render(<TimeRangePicker testID="picker" style={customStyle} />);
      expect(getByTestId("picker").props.style).toEqual(
        expect.arrayContaining([{ minHeight: 64 }, customStyle]),
      );
    });
  });
});
