import React, { FunctionComponent } from "react";
import type { StyleProp, ViewStyle } from "react-native";
import { StyleSheet } from "react-native";

import ColorViewNativeComponent, { type NativeColorViewProps } from "./ColorViewNativeComponent";

export type ColorViewProps = NativeColorViewProps & {
  color?: string;
  style?: StyleProp<ViewStyle>;
};

export const ColorView: FunctionComponent<ColorViewProps> = ({ color, style, ...props }) => (
  <ColorViewNativeComponent {...props} color={color} style={[styles.base, style]} />
);

const styles = StyleSheet.create({
  base: {
    width: 100,
    height: 100,
  },
});
