/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler } from "react-native/Libraries/Types/CodegenTypes";

/**
 * Event payload for the onDismiss event.
 */
export interface DismissEvent {}

/**
 * Native props for the ModalBottomSheet component.
 * Used by React Native codegen to generate native bindings.
 */
export interface NativeModalBottomSheetProps extends ViewProps {
  /** Controls the visibility of the bottom sheet. */
  visible?: boolean | null;
  /** Native event handler fired when the sheet is dismissed. */
  onDismiss?: DirectEventHandler<DismissEvent> | null;
}

/**
 * Native ModalBottomSheet component created via React Native codegen.
 * @internal
 */
export default codegenNativeComponent<NativeModalBottomSheetProps>(
  "ModalBottomSheet",
) as HostComponent<NativeModalBottomSheetProps>;
