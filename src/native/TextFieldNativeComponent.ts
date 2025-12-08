/* eslint-disable @typescript-eslint/no-empty-object-type */
/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler, Double } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Event payload for the onChangeText event.
 */
export type TextChangeEvent = Readonly<{
  text: string;
}>;

/**
 * Native props for the TextField component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeTextFieldProps extends ViewProps {
  /** Current text value */
  value?: string | null;
  /** Floating label text */
  label?: string | null;
  /** Placeholder text when empty */
  placeholder?: string | null;
  /** Whether the text field is disabled */
  disabled?: boolean | null;
  /** Whether text can be edited (maps to readOnly in Compose) */
  editable?: boolean | null;
  /** Enable multiline input */
  multiline?: boolean | null;
  /** Maximum character count */
  maxLength?: Double | null;
  /** Password field (hide text) */
  secureTextEntry?: boolean | null;
  /** Show error state styling */
  error?: boolean | null;
  /** Helper/error text below field */
  helperText?: string | null;

  /** Keyboard type: "default" | "email" | "number" | "phone" | "decimal" | "url" */
  keyboardType?: string | null;
  /** IME action button: "done" | "go" | "next" | "search" | "send" */
  returnKeyType?: string | null;
  /** Auto-capitalization: "none" | "sentences" | "words" | "characters" */
  autoCapitalize?: string | null;
  /** Enable/disable auto-correct */
  autoCorrect?: boolean | null;
  /** Leading icon name from Material Icons */
  leadingIcon?: string | null;
  /** Trailing icon name from Material Icons */
  trailingIcon?: string | null;
  /** Show character counter when maxLength is set */
  showCounter?: boolean | null;

  /** Native event handler fired when text changes */
  onTextFieldChange?: DirectEventHandler<TextChangeEvent> | null;
  /** Native event handler fired when field gains focus */
  onTextFieldFocus?: DirectEventHandler<Readonly<{}>> | null;
  /** Native event handler fired when field loses focus */
  onTextFieldBlur?: DirectEventHandler<Readonly<{}>> | null;
  /** Native event handler fired when IME action is pressed */
  onSubmitEditing?: DirectEventHandler<Readonly<{}>> | null;
  /** Native event handler fired when trailing icon is pressed */
  onTrailingIconPress?: DirectEventHandler<Readonly<{}>> | null;
}

/**
 * Native TextField component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeTextFieldProps>(
  "TextFieldView",
) as HostComponent<NativeTextFieldProps>;
