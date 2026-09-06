import { useMemo } from "react";
import { StyleSheet, type StyleProp, type ViewStyle } from "react-native";

/**
 * Base container styles shared by the native field components.
 * Compose content is measured by native; these keep the React Native
 * container from collapsing before the first layout pass.
 */
export const fieldStyles = StyleSheet.create({
  field: { minHeight: 64 },
  segmented: { minHeight: 48 },
});

/**
 * Merges a base style with the caller's style into a memoized array so the
 * native component sees a stable `style` prop identity across renders.
 */
export function useFieldStyle(
  base: StyleProp<ViewStyle>,
  style: StyleProp<ViewStyle> | undefined,
): StyleProp<ViewStyle> {
  return useMemo(() => [base, style], [base, style]);
}
