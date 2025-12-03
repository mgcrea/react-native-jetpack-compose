/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";
import type { DirectEventHandler, Double } from "react-native/Libraries/Types/CodegenTypes";

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
  /** Whether to show the drag handle at the top. Defaults to true. */
  showDragHandle?: boolean | null;
  /** Maximum height ratio (0-1) relative to screen height. Defaults to 0.9. */
  maxHeightRatio?: Double | null;
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
