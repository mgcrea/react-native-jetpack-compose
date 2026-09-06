import type { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useNativeEvent, useVoidEvent } from "../hooks/useNativeEvent";
import type { DateInput, YearRange } from "../types";
import { fromEpochMillis, toEpochMillis } from "../utils/date";
import { fieldStyles, useFieldStyle } from "../utils/style";
import DatePickerNativeComponent, {
  type ConfirmEvent,
  type DateChangeEvent,
  type NativeDatePickerProps,
} from "./DatePickerNativeComponent";

export type { YearRange } from "../types";

/**
 * Props for the DatePicker component.
 */
export type DatePickerProps = Omit<
  NativeDatePickerProps,
  | "selectedDateMillis"
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
  /** The selected date value. Accepts Date, epoch milliseconds, or ISO string. */
  value?: DateInput | null;
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
  /** Placeholder text when no date is selected. */
  placeholder?: string;
  /** Whether the picker field is disabled. */
  disabled?: boolean;
  /** Callback fired when user confirms selection with the selected date. */
  onConfirm?: (date: Date | null) => void;
  /** Callback fired when user cancels/dismisses the dialog. */
  onCancel?: () => void;
  /** Callback fired when the selected date changes (before confirmation). */
  onChange?: (date: Date | null) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

const toDate = (event: ConfirmEvent | DateChangeEvent) => fromEpochMillis(event.selectedDateMillis);

/**
 * A date picker component with a text field that opens a date picker dialog.
 *
 * Renders an OutlinedTextField that displays the selected date. When clicked,
 * opens a DatePickerDialog for date selection.
 *
 * @example
 * ```tsx
 * <DatePicker
 *   value={selectedDate}
 *   label="Birth Date"
 *   placeholder="Select a date"
 *   onConfirm={(date) => setSelectedDate(date)}
 *   onCancel={() => console.log('Cancelled')}
 * />
 * ```
 */
export const DatePicker: FunctionComponent<DatePickerProps> = ({
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
  const handleConfirm = useNativeEvent(onConfirm, toDate);
  const handleCancel = useVoidEvent(onCancel);
  const handleDateChange = useNativeEvent(onChange, toDate);

  return (
    <DatePickerNativeComponent
      {...props}
      selectedDateMillis={toEpochMillis(value)}
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
