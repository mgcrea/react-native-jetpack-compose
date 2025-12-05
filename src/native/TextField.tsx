import React, { FunctionComponent, useCallback } from "react";
import { type NativeSyntheticEvent, type StyleProp, type ViewStyle } from "react-native";

import TextFieldNativeComponent, {
  type NativeTextFieldProps,
  type TextChangeEvent,
} from "./TextFieldNativeComponent";

/**
 * Props for the TextField component.
 */
export type TextFieldProps = Omit<
  NativeTextFieldProps,
  | "disabled"
  | "editable"
  | "multiline"
  | "maxLength"
  | "secureTextEntry"
  | "error"
  | "onTextFieldChange"
  | "onTextFieldFocus"
  | "onTextFieldBlur"
> & {
  /** Current text value */
  value?: string;
  /** Floating label text */
  label?: string;
  /** Placeholder text when empty */
  placeholder?: string;
  /** Whether the text field is disabled */
  disabled?: boolean;
  /** Whether text can be edited */
  editable?: boolean;
  /** Enable multiline input */
  multiline?: boolean;
  /** Maximum character count */
  maxLength?: number;
  /** Password field (hide text) */
  secureTextEntry?: boolean;
  /** Show error state styling */
  error?: boolean;
  /** Helper/error text below field */
  helperText?: string;
  /** Callback fired when text changes */
  onChange?: (text: string) => void;
  /** Callback fired when field gains focus */
  onFocus?: () => void;
  /** Callback fired when field loses focus */
  onBlur?: () => void;
  /** Custom styles applied to the container */
  style?: StyleProp<ViewStyle>;
};

/**
 * A text field component powered by Jetpack Compose's OutlinedTextField.
 *
 * @example
 * ```tsx
 * <TextField
 *   value={name}
 *   label="Name"
 *   placeholder="Enter your name"
 *   onChange={setName}
 * />
 * ```
 */
export const TextField: FunctionComponent<TextFieldProps> = ({
  value,
  label,
  placeholder,
  disabled = false,
  editable = true,
  multiline = false,
  maxLength,
  secureTextEntry = false,
  error = false,
  helperText,
  onChange,
  onFocus,
  onBlur,
  style,
  ...props
}) => {
  const handleChange = useCallback(
    (event: NativeSyntheticEvent<TextChangeEvent>) => {
      onChange?.(event.nativeEvent.text);
    },
    [onChange],
  );

  const handleFocus = useCallback(() => {
    onFocus?.();
  }, [onFocus]);

  const handleBlur = useCallback(() => {
    onBlur?.();
  }, [onBlur]);

  // Calculate minHeight based on content
  const baseHeight = 56;
  const helperTextHeight = helperText ? 20 : 0;
  const multilineExtraHeight = multiline ? 48 : 0; // ~2 extra lines
  const minHeight = baseHeight + helperTextHeight + multilineExtraHeight;

  return (
    <TextFieldNativeComponent
      {...props}
      value={value}
      label={label}
      placeholder={placeholder}
      disabled={disabled}
      editable={editable}
      multiline={multiline}
      maxLength={maxLength}
      secureTextEntry={secureTextEntry}
      error={error}
      helperText={helperText}
      onTextFieldChange={handleChange}
      onTextFieldFocus={handleFocus}
      onTextFieldBlur={handleBlur}
      style={[{ minHeight }, style]}
    />
  );
};
