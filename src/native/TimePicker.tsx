import type { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useNativeEvent, useVoidEvent } from "../hooks/useNativeEvent";
import { fromMinutesFromMidnight, toMinutesFromMidnight } from "../utils/date";
import { fieldStyles, useFieldStyle } from "../utils/style";
import TimePickerNativeComponent, {
  type ConfirmEvent,
  type NativeTimePickerProps,
  type TimeChangeEvent,
} from "./TimePickerNativeComponent";

/**
 * Props for the TimePicker component.
 */
export type TimePickerProps = Omit<
  NativeTimePickerProps,
  "selectedTimeMinutes" | "onConfirm" | "onCancel" | "onTimeChange" | "titleText"
> & {
  /** The selected time value. */
  value?: Date | null;
  /** Whether to use 24-hour format. If not set, follows system locale. */
  is24Hour?: boolean;
  /** Initial display mode: 'picker' (dial) or 'input' (keyboard). */
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
  /** Placeholder text when no time is selected. */
  placeholder?: string;
  /** Whether the picker field is disabled. */
  disabled?: boolean;
  /** Callback fired when user confirms selection with the selected time. */
  onConfirm?: (time: Date | null) => void;
  /** Callback fired when user cancels/dismisses the dialog. */
  onCancel?: () => void;
  /** Callback fired when the selected time changes (before confirmation). */
  onChange?: (time: Date | null) => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
};

const toTime = (event: ConfirmEvent | TimeChangeEvent) => fromMinutesFromMidnight(event.selectedTimeMinutes);

/**
 * A time picker component with a text field that opens a time picker dialog.
 *
 * Renders an OutlinedTextField that displays the selected time. When clicked,
 * opens a TimePickerDialog for time selection.
 *
 * @example
 * ```tsx
 * <TimePicker
 *   value={selectedTime}
 *   label="Meeting Time"
 *   placeholder="Select a time"
 *   onConfirm={(time) => setSelectedTime(time)}
 *   onCancel={() => console.log('Cancelled')}
 * />
 * ```
 */
export const TimePicker: FunctionComponent<TimePickerProps> = ({
  value,
  is24Hour,
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
  const handleConfirm = useNativeEvent(onConfirm, toTime);
  const handleCancel = useVoidEvent(onCancel);
  const handleTimeChange = useNativeEvent(onChange, toTime);

  return (
    <TimePickerNativeComponent
      {...props}
      selectedTimeMinutes={toMinutesFromMidnight(value)}
      is24Hour={is24Hour}
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
      onTimeChange={handleTimeChange}
      style={useFieldStyle(fieldStyles.field, style)}
    />
  );
};
