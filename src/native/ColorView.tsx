import React, { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";
import { StyleSheet } from "react-native";

import ColorViewNativeComponent, { type NativeColorViewProps } from "./ColorViewNativeComponent";

/**
 * Props for the ColorView component.
 */
export type ColorViewProps = NativeColorViewProps & {
  /** The background color in hex format (e.g., "#FF5733"). Invalid values default to black. */
  color?: string;
  /** Custom styles applied to the view. */
  style?: StyleProp<ViewStyle>;
};

/**
 * A simple view component that displays a solid color background.
 * Powered by native Android View with Jetpack Compose integration.
 *
 * @example
 * ```tsx
 * <ColorView color="#FF5733" style={{ width: 200, height: 200 }} />
 * ```
 */
export const ColorView: FunctionComponent<ColorViewProps> = ({ color, style, ...props }) => (
  <ColorViewNativeComponent {...props} color={color} style={[styles.base, style]} />
);

const styles = StyleSheet.create({
  base: {
    width: 100,
    height: 100,
  },
});
