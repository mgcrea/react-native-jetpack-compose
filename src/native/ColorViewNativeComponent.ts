/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";

/**
 * Native props for the ColorView component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeColorViewProps extends ViewProps {
  /** The background color in hex format (e.g., "#FF5733"). */
  color?: string | null;
}

/**
 * Native ColorView component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeColorViewProps>(
  "ColorView",
) as HostComponent<NativeColorViewProps>;
