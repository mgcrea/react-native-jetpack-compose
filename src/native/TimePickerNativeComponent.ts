import { codegenNativeComponent, type CodegenTypes, type HostComponent, type ViewProps } from "react-native";

/**
 * Event payload for the onConfirm event.
 */
export interface ConfirmEvent {
  selectedTimeMinutes: CodegenTypes.Int32;
}

/**
 * Event payload for the onCancel event.
 */
export interface CancelEvent {}

/**
 * Event payload for the onTimeChange event.
 */
export interface TimeChangeEvent {
  selectedTimeMinutes: CodegenTypes.Int32;
}

/**
 * Native props for the TimePicker component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeTimePickerProps extends ViewProps {
  /** The selected time as minutes from midnight (0-1439). */
  selectedTimeMinutes?: CodegenTypes.WithDefault<CodegenTypes.Int32, -1>;

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
  onConfirm?: CodegenTypes.DirectEventHandler<ConfirmEvent> | null;
  /** Native event handler fired when user cancels/dismisses. */
  onCancel?: CodegenTypes.DirectEventHandler<CancelEvent> | null;
  /** Native event handler fired when selection changes. */
  onTimeChange?: CodegenTypes.DirectEventHandler<TimeChangeEvent> | null;
}

/**
 * Native TimePicker component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeTimePickerProps>(
  "TimePickerView",
) as HostComponent<NativeTimePickerProps>;
