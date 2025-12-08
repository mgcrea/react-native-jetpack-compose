import { describe, expect, it, jest } from "@jest/globals";
import { render } from "@testing-library/react-native";
import React from "react";
import type { NativeSyntheticEvent } from "react-native";
import { Text } from "react-native";

import { ModalBottomSheet } from "./ModalBottomSheet";
import type { DismissEvent } from "./ModalBottomSheetNativeComponent";

// Helper to create mock native events
function createNativeEvent<T>(payload: T): NativeSyntheticEvent<T> {
  return { nativeEvent: payload } as NativeSyntheticEvent<T>;
}

describe("ModalBottomSheet", () => {
  describe("default props", () => {
    it("visible defaults to false", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.visible).toBe(false);
    });

    it("showDragHandle defaults to true", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.showDragHandle).toBe(true);
    });

    it("maxHeightRatio defaults to 0.9", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.maxHeightRatio).toBe(0.9);
    });
  });

  describe("event handlers", () => {
    it("onDismiss is called without arguments", () => {
      const onDismiss = jest.fn();
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" onDismiss={onDismiss} />);
      const sheet = getByTestId("sheet");

      const event = createNativeEvent<DismissEvent>({});
      sheet.props.onDismiss(event);

      expect(onDismiss).toHaveBeenCalledTimes(1);
      expect(onDismiss).toHaveBeenCalledWith();
    });

    it("does not throw when onDismiss is not provided", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" />);
      const sheet = getByTestId("sheet");

      const event = createNativeEvent<DismissEvent>({});
      expect(() => sheet.props.onDismiss(event)).not.toThrow();
    });
  });

  describe("prop passthrough", () => {
    it("passes visible as true when set", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" visible />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.visible).toBe(true);
    });

    it("passes showDragHandle as false when set", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" showDragHandle={false} />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.showDragHandle).toBe(false);
    });

    it("passes custom maxHeightRatio", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" maxHeightRatio={0.5} />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.maxHeightRatio).toBe(0.5);
    });
  });

  describe("children rendering", () => {
    it("renders children", () => {
      const { getByText } = render(
        <ModalBottomSheet testID="sheet">
          <Text>Sheet Content</Text>
        </ModalBottomSheet>,
      );
      expect(getByText("Sheet Content")).toBeTruthy();
    });

    it("renders multiple children", () => {
      const { getByText } = render(
        <ModalBottomSheet testID="sheet">
          <Text>First</Text>
          <Text>Second</Text>
        </ModalBottomSheet>,
      );
      expect(getByText("First")).toBeTruthy();
      expect(getByText("Second")).toBeTruthy();
    });

    it("renders without children", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" />);
      expect(getByTestId("sheet")).toBeTruthy();
    });
  });

  describe("style handling", () => {
    it("passes custom style", () => {
      const customStyle = { backgroundColor: "white" };
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" style={customStyle} />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.style).toEqual(customStyle);
    });

    it("handles undefined style", () => {
      const { getByTestId } = render(<ModalBottomSheet testID="sheet" />);
      const sheet = getByTestId("sheet");
      expect(sheet.props.style).toBeUndefined();
    });
  });
});
