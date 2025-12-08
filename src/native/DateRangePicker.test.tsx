import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import React from "react";
import type { NativeSyntheticEvent } from "react-native";

import { DateRangePicker } from "./DateRangePicker";
import type {
  RangeCancelEvent,
  RangeConfirmEvent,
  RangeDateChangeEvent,
} from "./DateRangePickerNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

describe("DateRangePicker", () => {
  describe("toEpochMillis conversion (via value prop)", () => {
    it("handles undefined value", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" value={undefined} />);
      const picker = getByTestId("picker");
      expect(picker.props.startDateMillis).toBeUndefined();
      expect(picker.props.endDateMillis).toBeUndefined();
    });

    it("handles null value", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" value={null} />);
      const picker = getByTestId("picker");
      expect(picker.props.startDateMillis).toBeUndefined();
      expect(picker.props.endDateMillis).toBeUndefined();
    });

    it("handles DateRange with Date objects", () => {
      const startDate = new Date("2024-01-01T00:00:00.000Z");
      const endDate = new Date("2024-01-15T00:00:00.000Z");
      const { getByTestId } = render(<DateRangePicker testID="picker" value={{ startDate, endDate }} />);
      const picker = getByTestId("picker");
      expect(picker.props.startDateMillis).toBe(startDate.getTime());
      expect(picker.props.endDateMillis).toBe(endDate.getTime());
    });

    it("handles DateRange with null dates", () => {
      const { getByTestId } = render(
        <DateRangePicker testID="picker" value={{ startDate: null, endDate: null }} />,
      );
      const picker = getByTestId("picker");
      expect(picker.props.startDateMillis).toBeUndefined();
      expect(picker.props.endDateMillis).toBeUndefined();
    });

    it("handles DateRange with mixed null/Date values", () => {
      const startDate = new Date("2024-01-01T00:00:00.000Z");
      const { getByTestId } = render(<DateRangePicker testID="picker" value={{ startDate, endDate: null }} />);
      const picker = getByTestId("picker");
      expect(picker.props.startDateMillis).toBe(startDate.getTime());
      expect(picker.props.endDateMillis).toBeUndefined();
    });
  });

  describe("date constraint props", () => {
    it("converts minDate to milliseconds", () => {
      const minDate = new Date("2024-01-01");
      const { getByTestId } = render(<DateRangePicker testID="picker" minDate={minDate} />);
      const picker = getByTestId("picker");
      expect(picker.props.minDateMillis).toBe(minDate.getTime());
    });

    it("converts maxDate to milliseconds", () => {
      const maxDate = new Date("2024-12-31");
      const { getByTestId } = render(<DateRangePicker testID="picker" maxDate={maxDate} />);
      const picker = getByTestId("picker");
      expect(picker.props.maxDateMillis).toBe(maxDate.getTime());
    });

    it("converts initialDisplayedMonth to milliseconds", () => {
      const month = new Date("2024-06-15");
      const { getByTestId } = render(<DateRangePicker testID="picker" initialDisplayedMonth={month} />);
      const picker = getByTestId("picker");
      expect(picker.props.initialDisplayedMonthMillis).toBe(month.getTime());
    });

    it("passes yearRange as separate props", () => {
      const yearRange = { start: 2020, end: 2030 };
      const { getByTestId } = render(<DateRangePicker testID="picker" yearRange={yearRange} />);
      const picker = getByTestId("picker");
      expect(picker.props.yearRangeStart).toBe(2020);
      expect(picker.props.yearRangeEnd).toBe(2030);
    });
  });

  describe("event handlers", () => {
    it("onConfirm receives DateRange with Date objects", () => {
      const onConfirm = jest.fn();
      const { getByTestId } = render(<DateRangePicker testID="picker" onConfirm={onConfirm} />);
      const picker = getByTestId("picker");

      const startEpoch = 1704067200000;
      const endEpoch = 1705276800000;
      const event = createNativeEvent<RangeConfirmEvent>({
        startDateMillis: startEpoch,
        endDateMillis: endEpoch,
      });
      picker.props.onConfirm(event);

      expect(onConfirm).toHaveBeenCalledTimes(1);
      expect(onConfirm).toHaveBeenCalledWith({
        startDate: new Date(startEpoch),
        endDate: new Date(endEpoch),
      });
    });

    it("onConfirm handles null dates when millis are falsy", () => {
      const onConfirm = jest.fn();
      const { getByTestId } = render(<DateRangePicker testID="picker" onConfirm={onConfirm} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<RangeConfirmEvent>({
        startDateMillis: 0,
        endDateMillis: 0,
      });
      picker.props.onConfirm(event);

      expect(onConfirm).toHaveBeenCalledWith({
        startDate: null,
        endDate: null,
      });
    });

    it("onCancel is called without arguments", () => {
      const onCancel = jest.fn();
      const { getByTestId } = render(<DateRangePicker testID="picker" onCancel={onCancel} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<RangeCancelEvent>({});
      picker.props.onCancel(event);

      expect(onCancel).toHaveBeenCalledTimes(1);
      expect(onCancel).toHaveBeenCalledWith();
    });

    it("onChange receives DateRange with Date objects", () => {
      const onChange = jest.fn();
      const { getByTestId } = render(<DateRangePicker testID="picker" onChange={onChange} />);
      const picker = getByTestId("picker");

      const startEpoch = 1704067200000;
      const endEpoch = 1705276800000;
      const event = createNativeEvent<RangeDateChangeEvent>({
        startDateMillis: startEpoch,
        endDateMillis: endEpoch,
      });
      picker.props.onDateChange(event);

      expect(onChange).toHaveBeenCalledWith({
        startDate: new Date(startEpoch),
        endDate: new Date(endEpoch),
      });
    });

    it("onChange handles null dates when millis are falsy", () => {
      const onChange = jest.fn();
      const { getByTestId } = render(<DateRangePicker testID="picker" onChange={onChange} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<RangeDateChangeEvent>({
        startDateMillis: 0,
        endDateMillis: 0,
      });
      picker.props.onDateChange(event);

      expect(onChange).toHaveBeenCalledWith({
        startDate: null,
        endDate: null,
      });
    });
  });

  describe("default props", () => {
    it("initialDisplayMode defaults to picker", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" />);
      const picker = getByTestId("picker");
      expect(picker.props.initialDisplayMode).toBe("picker");
    });

    it("showModeToggle defaults to true", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" />);
      const picker = getByTestId("picker");
      expect(picker.props.showModeToggle).toBe(true);
    });
  });

  describe("prop passthrough", () => {
    it("passes title as titleText", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" title="Select Dates" />);
      const picker = getByTestId("picker");
      expect(picker.props.titleText).toBe("Select Dates");
    });

    it("passes label prop", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" label="Trip Dates" />);
      const picker = getByTestId("picker");
      expect(picker.props.label).toBe("Trip Dates");
    });

    it("passes placeholder prop", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" placeholder="Select dates" />);
      const picker = getByTestId("picker");
      expect(picker.props.placeholder).toBe("Select dates");
    });

    it("passes disabled prop", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" disabled />);
      const picker = getByTestId("picker");
      expect(picker.props.disabled).toBe(true);
    });
  });

  describe("style handling", () => {
    it("applies base minHeight style", () => {
      const { getByTestId } = render(<DateRangePicker testID="picker" />);
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }]));
    });

    it("merges custom style with base style", () => {
      const customStyle = { backgroundColor: "red" };
      const { getByTestId } = render(<DateRangePicker testID="picker" style={customStyle} />);
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }, customStyle]));
    });
  });
});
