import { useMemo, type FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useNativeEvent, useVoidEvent } from "../hooks/useNativeEvent";
import { useFieldStyle } from "../utils/style";
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
  | "autoCorrect"
  | "showCounter"
  | "onTextFieldChange"
  | "onTextFieldFocus"
  | "onTextFieldBlur"
  | "onSubmitEditing"
  | "onTrailingIconPress"
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

  /** Keyboard type for input. Default: "default" */
  keyboardType?: "default" | "email" | "number" | "phone" | "decimal" | "url";
  /** IME action button type. Default: "done" */
  returnKeyType?: "done" | "go" | "next" | "search" | "send";
  /** Auto-capitalization mode. Default: "sentences" */
  autoCapitalize?: "none" | "sentences" | "words" | "characters";
  /** Auto-correct enabled. Default: true */
  autoCorrect?: boolean;
  /** Leading icon name (Material Icons). e.g., "Search", "Email", "Phone" */
  leadingIcon?: string;
  /** Trailing icon name (Material Icons). e.g., "Clear", "Visibility" */
  trailingIcon?: string;
  /** Show character counter when maxLength is set. Default: true */
  showCounter?: boolean;

  /** Callback fired when text changes */
  onChange?: (text: string) => void;
  /** Callback fired when field gains focus */
  onFocus?: () => void;
  /** Callback fired when field loses focus */
  onBlur?: () => void;
  /** Callback fired when IME action button is pressed */
  onSubmitEditing?: () => void;
  /** Callback fired when trailing icon is pressed */
  onTrailingIconPress?: () => void;
  /** Custom styles applied to the container */
  style?: StyleProp<ViewStyle>;
};

const toText = (event: TextChangeEvent) => event.text;

const BASE_HEIGHT = 64;
const HELPER_TEXT_HEIGHT = 20;
const MULTILINE_EXTRA_HEIGHT = 48; // ~2 extra lines

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
  keyboardType = "default",
  returnKeyType = "done",
  autoCapitalize = "sentences",
  autoCorrect = true,
  leadingIcon,
  trailingIcon,
  showCounter = true,
  onChange,
  onFocus,
  onBlur,
  onSubmitEditing,
  onTrailingIconPress,
  style,
  ...props
}) => {
  const handleChange = useNativeEvent(onChange, toText);
  const handleFocus = useVoidEvent(onFocus);
  const handleBlur = useVoidEvent(onBlur);
  const handleSubmitEditing = useVoidEvent(onSubmitEditing);
  const handleTrailingIconPress = useVoidEvent(onTrailingIconPress);

  // Reserve room for the helper/counter row and extra lines so the container does not clip
  const hasHelperRow = Boolean(helperText) || (showCounter && maxLength != null);
  const minHeight =
    BASE_HEIGHT + (hasHelperRow ? HELPER_TEXT_HEIGHT : 0) + (multiline ? MULTILINE_EXTRA_HEIGHT : 0);
  const baseStyle = useMemo(() => ({ minHeight }), [minHeight]);

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
      keyboardType={keyboardType}
      returnKeyType={returnKeyType}
      autoCapitalize={autoCapitalize}
      autoCorrect={autoCorrect}
      leadingIcon={leadingIcon}
      trailingIcon={trailingIcon}
      showCounter={showCounter}
      onTextFieldChange={handleChange}
      onTextFieldFocus={handleFocus}
      onTextFieldBlur={handleBlur}
      onSubmitEditing={handleSubmitEditing}
      onTrailingIconPress={handleTrailingIconPress}
      style={useFieldStyle(baseStyle, style)}
    />
  );
};
