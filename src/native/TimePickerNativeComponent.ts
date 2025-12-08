/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler, Int32 } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Event payload for the onConfirm event.
 */
export interface ConfirmEvent {
  selectedTimeMinutes: Int32;
}

/**
 * Event payload for the onCancel event.
 */
// eslint-disable-next-line @typescript-eslint/no-empty-object-type
export interface CancelEvent {}

/**
 * Event payload for the onTimeChange event.
 */
export interface TimeChangeEvent {
  selectedTimeMinutes: Int32;
}

/**
 * Native props for the TimePicker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeTimePickerProps extends ViewProps {
  /** The selected time as minutes from midnight (0-1439). */
  selectedTimeMinutes?: Int32 | null;

  /** Whether to use 24-hour format. If not set, follows system locale. */
  is24Hour?: boolean | null;

  /** Initial display mode: 'picker' (dial) or 'input' (keyboard). */
  initialDisplayMode?: string | null;
  /** Whether to show the mode toggle button. */
  showModeToggle?: boolean | null;

  /** Label for the confirm button. */
  confirmLabel?: string | null;
  /** Label for the cancel button. */
  cancelLabel?: string | null;
  /** Title text displayed at the top of the picker. */
  titleText?: string | null;

  /** Floating label text for the text field. */
  label?: string | null;
  /** Placeholder text when no value selected. */
  placeholder?: string | null;
  /** Whether the picker is disabled. */
  disabled?: boolean | null;

  /** Native event handler fired when user confirms selection. */
  onConfirm?: DirectEventHandler<ConfirmEvent> | null;
  /** Native event handler fired when user cancels/dismisses. */
  onCancel?: DirectEventHandler<CancelEvent> | null;
  /** Native event handler fired when selection changes. */
  onTimeChange?: DirectEventHandler<TimeChangeEvent> | null;
}

/**
 * Native TimePicker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeTimePickerProps>(
  "TimePickerView",
) as HostComponent<NativeTimePickerProps>;
