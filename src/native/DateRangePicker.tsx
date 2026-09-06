import type { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useNativeEvent, useVoidEvent } from "../hooks/useNativeEvent";
import type { DateInput, DateRange, YearRange } from "../types";
import { fromEpochMillis, toEpochMillis } from "../utils/date";
import { fieldStyles, useFieldStyle } from "../utils/style";
import DateRangePickerNativeComponent, {
  type NativeDateRangePickerProps,
  type RangeConfirmEvent,
  type RangeDateChangeEvent,
} from "./DateRangePickerNativeComponent";

export type { DateRange, YearRange } from "../types";

/**
 * Props for the DateRangePicker component.
 */
export type DateRangePickerProps = Omit<
  NativeDateRangePickerProps,
  | "startDateMillis"
  | "endDateMillis"
  | "initialDisplayedMonthMillis"
  | "minDateMillis"
  | "maxDateMillis"
  | "yearRangeStart"
  | "yearRangeEnd"
  | "onConfirm"
  | "onCancel"
  | "onDateChange"
  | "titleText"
> & {
  /** The selected date range value. */
  value?: DateRange | null;
  /** The initially displayed month. Accepts Date, epoch milliseconds, or ISO string. */
  initialDisplayedMonth?: DateInput;
  /** Minimum selectable date. Accepts Date, epoch milliseconds, or ISO string. */
  minDate?: DateInput;
  /** Maximum selectable date. Accepts Date, epoch milliseconds, or ISO string. */
  maxDate?: DateInput;
  /** Year range for the year selector. */
  yearRange?: YearRange;
  /** Initial display mode: 'picker' (calendar) or 'input' (text field). */
  initialDisplayMode?: "picker" | "input";
  /** Whether to show the mode toggle button. Defaults to true. */
  showModeToggle?: boolean;
  /** Label for the confirm button. Defaults to "OK". */
  confirmLabel?: string;
  /** Label for the cancel button. Defaults to "Cancel". */
  cancelLabel?: string;
  /** Title text displayed at the top of the picker dialog. */
  title?: string;
  /** Floating label text for the text field. */
  label?: string;
  /** Placeholder text when no date range is selected. */
  placeholder?: string;
  /** Whether the picker field is disabled. */
  disabled?: boolean;
  /** Callback fired when user confirms selection with the selected date range. */
  onConfirm?: (range: DateRange) => void;
  /** Callback fired when user cancels/dismisses the dialog. */
  onCancel?: () => void;
  /** Callback fired when the selected date range changes (before confirmation). */
  onChange?: (range: DateRange) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

const toDateRange = (event: RangeConfirmEvent | RangeDateChangeEvent): DateRange => {
  return {
    startDate: fromEpochMillis(event.startDateMillis),
    endDate: fromEpochMillis(event.endDateMillis),
  };
};

/**
 * A date range picker component with a text field that opens a date range picker dialog.
 *
 * Renders an OutlinedTextField that displays the selected date range. When clicked,
 * opens a DateRangePickerDialog for date range selection.
 *
 * @example
 * ```tsx
 * <DateRangePicker
 *   value={{ startDate, endDate }}
 *   label="Trip Dates"
 *   placeholder="Select dates"
 *   onConfirm={(range) => {
 *     setStartDate(range.startDate);
 *     setEndDate(range.endDate);
 *   }}
 *   onCancel={() => console.log('Cancelled')}
 * />
 * ```
 */
export const DateRangePicker: FunctionComponent<DateRangePickerProps> = ({
  value,
  initialDisplayedMonth,
  minDate,
  maxDate,
  yearRange,
  initialDisplayMode = "picker",
  showModeToggle = true,
  confirmLabel,
  cancelLabel,
  title,
  label,
  placeholder,
  disabled,
  onConfirm,
  onCancel,
  onChange,
  style,
  ...props
}) => {
  const handleConfirm = useNativeEvent(onConfirm, toDateRange);
  const handleCancel = useVoidEvent(onCancel);
  const handleDateChange = useNativeEvent(onChange, toDateRange);

  return (
    <DateRangePickerNativeComponent
      {...props}
      startDateMillis={toEpochMillis(value?.startDate)}
      endDateMillis={toEpochMillis(value?.endDate)}
      initialDisplayedMonthMillis={toEpochMillis(initialDisplayedMonth)}
      minDateMillis={toEpochMillis(minDate)}
      maxDateMillis={toEpochMillis(maxDate)}
      yearRangeStart={yearRange?.start}
      yearRangeEnd={yearRange?.end}
      initialDisplayMode={initialDisplayMode}
      showModeToggle={showModeToggle}
      confirmLabel={confirmLabel}
      cancelLabel={cancelLabel}
      titleText={title}
      label={label}
      placeholder={placeholder}
      disabled={disabled}
      onConfirm={handleConfirm}
      onCancel={handleCancel}
      onDateChange={handleDateChange}
      style={useFieldStyle(fieldStyles.field, style)}
    />
  );
};
