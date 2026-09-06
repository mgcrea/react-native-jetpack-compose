import type { FunctionComponent, ReactNode } from "react";
import type { StyleProp, ViewStyle } from "react-native";

import { useVoidEvent } from "../hooks/useNativeEvent";
import ModalBottomSheetNativeComponent, {
  type NativeModalBottomSheetProps,
} from "./ModalBottomSheetNativeComponent";

/**
 * Props for the ModalBottomSheet component.
 */
export type ModalBottomSheetProps = Omit<
  NativeModalBottomSheetProps,
  "onDismiss" | "showDragHandle" | "maxHeightRatio"
> & {
  /** Controls the visibility of the bottom sheet. */
  visible?: boolean;
  /** Whether to show the drag handle at the top. Defaults to true. */
  showDragHandle?: boolean;
  /** Maximum height ratio (0-1) relative to screen height. Defaults to 0.9. */
  maxHeightRatio?: number;
  /** Callback fired when the sheet is dismissed (by user gesture or programmatically). */
  onDismiss?: () => void;
  /** Custom styles applied to the container. */
  style?: StyleProp<ViewStyle>;
  /** Content to render inside the bottom sheet. */
  children?: ReactNode;
};

/**
 * A modal bottom sheet component powered by Android's BottomSheetDialog.
 *
 * @example
 * ```tsx
 * <ModalBottomSheet visible={isOpen} onDismiss={() => setIsOpen(false)}>
 *   <Text>Sheet Content</Text>
 * </ModalBottomSheet>
 * ```
 */
export const ModalBottomSheet: FunctionComponent<ModalBottomSheetProps> = ({
  visible = false,
  showDragHandle = true,
  maxHeightRatio = 0.9,
  onDismiss,
  style,
  children,
  ...props
}) => {
  const handleDismiss = useVoidEvent(onDismiss);

  return (
    <ModalBottomSheetNativeComponent
      {...props}
      visible={visible}
      showDragHandle={showDragHandle}
      maxHeightRatio={maxHeightRatio}
      onDismiss={handleDismiss}
      style={style}
    >
      {children}
    </ModalBottomSheetNativeComponent>
  );
};
