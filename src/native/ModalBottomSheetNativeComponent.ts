/* eslint-disable @typescript-eslint/consistent-type-definitions */
import type { DirectEventHandler } from "react-native/Libraries/Types/CodegenTypes";
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";

export interface NativeModalBottomSheetProps extends ViewProps {
  visible?: boolean | null;
  onDismiss?: DirectEventHandler<{}> | null;
}

export default codegenNativeComponent<NativeModalBottomSheetProps>(
  "ModalBottomSheet",
) as HostComponent<NativeModalBottomSheetProps>;
