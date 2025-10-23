/* eslint-disable @typescript-eslint/consistent-type-definitions */
import { codegenNativeComponent, type HostComponent, type ViewProps } from "react-native";

export interface NativeColorViewProps extends ViewProps {
  color?: string | null;
}

export default codegenNativeComponent<NativeColorViewProps>(
  "ColorView",
) as HostComponent<NativeColorViewProps>;
