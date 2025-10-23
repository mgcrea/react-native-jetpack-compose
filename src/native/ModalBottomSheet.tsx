import React, { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";
import { StyleSheet } from "react-native";

import ModalBottomSheetNativeComponent, {
  type NativeModalBottomSheetProps,
} from "./ModalBottomSheetNativeComponent";

export type ModalBottomSheetProps = NativeModalBottomSheetProps & {
  visible?: boolean;
  onDismiss?: () => void;
  style?: StyleProp<ViewStyle>;
  children?: React.ReactNode;
};

export const ModalBottomSheet: FunctionComponent<ModalBottomSheetProps> = ({
  visible = false,
  onDismiss,
  style,
  children,
  ...props
}) => (
  <ModalBottomSheetNativeComponent
    {...props}
    visible={visible}
    onDismiss={onDismiss}
    style={[styles.base, style]}
  >
    {children}
  </ModalBottomSheetNativeComponent>
);

const styles = StyleSheet.create({
  base: {
    // Default styles for the bottom sheet container
  },
});
