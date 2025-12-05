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

  /** Native event handler fired when text changes */
  onTextFieldChange?: DirectEventHandler<TextChangeEvent> | null;
  /** Native event handler fired when field gains focus */
  onTextFieldFocus?: DirectEventHandler<Readonly<{}>> | null;
  /** Native event handler fired when field loses focus */
  onTextFieldBlur?: DirectEventHandler<Readonly<{}>> | null;
}

/**
 * Native TextField component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeTextFieldProps>(
  "TextFieldView",
) as HostComponent<NativeTextFieldProps>;
