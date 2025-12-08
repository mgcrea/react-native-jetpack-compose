import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import React from "react";
import type { NativeSyntheticEvent } from "react-native";

import { DatePicker } from "./DatePicker";
import type { CancelEvent, ConfirmEvent, DateChangeEvent } from "./DatePickerNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

describe("DatePicker", () => {
  describe("toEpochMillis conversion (via value prop)", () => {
    it("handles undefined value", () => {
      const { getByTestId } = render(<DatePicker testID="picker" value={undefined} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedDateMillis).toBeUndefined();
    });

    it("handles null value", () => {
      const { getByTestId } = render(<DatePicker testID="picker" value={null} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedDateMillis).toBeUndefined();
    });

    it("handles number (epoch milliseconds)", () => {
      const epoch = 1704067200000; // 2024-01-01T00:00:00.000Z
      const { getByTestId } = render(<DatePicker testID="picker" value={epoch} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedDateMillis).toBe(epoch);
    });

    it("handles ISO string", () => {
      const isoString = "2024-01-01T00:00:00.000Z";
      const expectedEpoch = new Date(isoString).getTime();
      const { getByTestId } = render(<DatePicker testID="picker" value={isoString} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedDateMillis).toBe(expectedEpoch);
    });

    it("handles Date object", () => {
      const date = new Date("2024-01-01T00:00:00.000Z");
      const { getByTestId } = render(<DatePicker testID="picker" value={date} />);
      const picker = getByTestId("picker");
      expect(picker.props.selectedDateMillis).toBe(date.getTime());
    });
  });

  describe("date constraint props", () => {
    it("converts minDate to milliseconds", () => {
      const minDate = new Date("2024-01-01");
      const { getByTestId } = render(<DatePicker testID="picker" minDate={minDate} />);
      const picker = getByTestId("picker");
      expect(picker.props.minDateMillis).toBe(minDate.getTime());
    });

    it("converts maxDate to milliseconds", () => {
      const maxDate = new Date("2024-12-31");
      const { getByTestId } = render(<DatePicker testID="picker" maxDate={maxDate} />);
      const picker = getByTestId("picker");
      expect(picker.props.maxDateMillis).toBe(maxDate.getTime());
    });

    it("converts initialDisplayedMonth to milliseconds", () => {
      const month = new Date("2024-06-15");
      const { getByTestId } = render(<DatePicker testID="picker" initialDisplayedMonth={month} />);
      const picker = getByTestId("picker");
      expect(picker.props.initialDisplayedMonthMillis).toBe(month.getTime());
    });

    it("passes yearRange as separate props", () => {
      const yearRange = { start: 2020, end: 2030 };
      const { getByTestId } = render(<DatePicker testID="picker" yearRange={yearRange} />);
      const picker = getByTestId("picker");
      expect(picker.props.yearRangeStart).toBe(2020);
      expect(picker.props.yearRangeEnd).toBe(2030);
    });
  });

  describe("event handlers", () => {
    it("onConfirm receives Date object from millis", () => {
      const onConfirm = jest.fn();
      const { getByTestId } = render(<DatePicker testID="picker" onConfirm={onConfirm} />);
      const picker = getByTestId("picker");

      const epoch = 1704067200000;
      const event = createNativeEvent<ConfirmEvent>({ selectedDateMillis: epoch });
      picker.props.onConfirm(event);

      expect(onConfirm).toHaveBeenCalledTimes(1);
      expect(onConfirm).toHaveBeenCalledWith(new Date(epoch));
    });

    it("onConfirm receives null when millis is falsy", () => {
      const onConfirm = jest.fn();
      const { getByTestId } = render(<DatePicker testID="picker" onConfirm={onConfirm} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<ConfirmEvent>({ selectedDateMillis: 0 });
      picker.props.onConfirm(event);

      expect(onConfirm).toHaveBeenCalledWith(null);
    });

    it("onCancel is called without arguments", () => {
      const onCancel = jest.fn();
      const { getByTestId } = render(<DatePicker testID="picker" onCancel={onCancel} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<CancelEvent>({});
      picker.props.onCancel(event);

      expect(onCancel).toHaveBeenCalledTimes(1);
      expect(onCancel).toHaveBeenCalledWith();
    });

    it("onChange receives Date object from millis", () => {
      const onChange = jest.fn();
      const { getByTestId } = render(<DatePicker testID="picker" onChange={onChange} />);
      const picker = getByTestId("picker");

      const epoch = 1704067200000;
      const event = createNativeEvent<DateChangeEvent>({ selectedDateMillis: epoch });
      picker.props.onDateChange(event);

      expect(onChange).toHaveBeenCalledWith(new Date(epoch));
    });

    it("onChange receives null when millis is falsy", () => {
      const onChange = jest.fn();
      const { getByTestId } = render(<DatePicker testID="picker" onChange={onChange} />);
      const picker = getByTestId("picker");

      const event = createNativeEvent<DateChangeEvent>({ selectedDateMillis: 0 });
      picker.props.onDateChange(event);

      expect(onChange).toHaveBeenCalledWith(null);
    });
  });

  describe("default props", () => {
    it("initialDisplayMode defaults to picker", () => {
      const { getByTestId } = render(<DatePicker testID="picker" />);
      const picker = getByTestId("picker");
      expect(picker.props.initialDisplayMode).toBe("picker");
    });

    it("showModeToggle defaults to true", () => {
      const { getByTestId } = render(<DatePicker testID="picker" />);
      const picker = getByTestId("picker");
      expect(picker.props.showModeToggle).toBe(true);
    });
  });

  describe("prop passthrough", () => {
    it("passes title as titleText", () => {
      const { getByTestId } = render(<DatePicker testID="picker" title="Select Date" />);
      const picker = getByTestId("picker");
      expect(picker.props.titleText).toBe("Select Date");
    });

    it("passes label prop", () => {
      const { getByTestId } = render(<DatePicker testID="picker" label="Birth Date" />);
      const picker = getByTestId("picker");
      expect(picker.props.label).toBe("Birth Date");
    });

    it("passes placeholder prop", () => {
      const { getByTestId } = render(<DatePicker testID="picker" placeholder="Select a date" />);
      const picker = getByTestId("picker");
      expect(picker.props.placeholder).toBe("Select a date");
    });

    it("passes disabled prop", () => {
      const { getByTestId } = render(<DatePicker testID="picker" disabled />);
      const picker = getByTestId("picker");
      expect(picker.props.disabled).toBe(true);
    });

    it("passes confirmLabel prop", () => {
      const { getByTestId } = render(<DatePicker testID="picker" confirmLabel="Done" />);
      const picker = getByTestId("picker");
      expect(picker.props.confirmLabel).toBe("Done");
    });

    it("passes cancelLabel prop", () => {
      const { getByTestId } = render(<DatePicker testID="picker" cancelLabel="Dismiss" />);
      const picker = getByTestId("picker");
      expect(picker.props.cancelLabel).toBe("Dismiss");
    });
  });

  describe("style handling", () => {
    it("applies base minHeight style", () => {
      const { getByTestId } = render(<DatePicker testID="picker" />);
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }]));
    });

    it("merges custom style with base style", () => {
      const customStyle = { backgroundColor: "red" };
      const { getByTestId } = render(<DatePicker testID="picker" style={customStyle} />);
      const picker = getByTestId("picker");
      expect(picker.props.style).toEqual(expect.arrayContaining([{ minHeight: 64 }, customStyle]));
    });
  });
});
