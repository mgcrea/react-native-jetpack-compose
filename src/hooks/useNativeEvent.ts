import { useCallback } from "react";
import type { NativeSyntheticEvent } from "react-native";

/**
 * Wraps a caller-provided handler so it receives a mapped payload instead of the
 * raw native event. `map` should be a stable function (module-level or memoized);
 * the returned callback changes identity only when `handler` or `map` do.
 */
export function useNativeEvent<T, R>(
  handler: ((value: R) => void) | undefined,
  map: (payload: T) => R,
): (event: NativeSyntheticEvent<T>) => void {
  return useCallback(
    (event: NativeSyntheticEvent<T>) => {
      handler?.(map(event.nativeEvent));
    },
    [handler, map],
  );
}

/**
 * Wraps a zero-argument caller handler so it is invoked with no arguments
 * regardless of the native event payload.
 */
export function useVoidEvent(
  handler: (() => void) | undefined,
): (event?: NativeSyntheticEvent<unknown>) => void {
  return useCallback(() => {
    handler?.();
  }, [handler]);
}
