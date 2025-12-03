# @mgcrea/react-native-jetpack-compose

Jetpack Compose components for React Native. This library provides native Android UI components powered by Jetpack Compose, accessible from React Native.

## Features

- **ColorView** - A simple view with a solid color background
- **ModalBottomSheet** - A Material 3 modal bottom sheet with gesture support

## Requirements

- React Native 0.76+ (New Architecture support)
- Android SDK 21+ (minSdk)
- Android SDK 34+ (compileSdk)

## Installation

```bash
npm install @mgcrea/react-native-jetpack-compose
# or
yarn add @mgcrea/react-native-jetpack-compose
# or
pnpm add @mgcrea/react-native-jetpack-compose
```

The library will auto-link with React Native. No additional setup is required.

## Usage

### ColorView

A simple view component that displays a solid color background.

```tsx
import { ColorView } from "@mgcrea/react-native-jetpack-compose";

function MyComponent() {
  return (
    <ColorView
      color="#FF5733"
      style={{ width: 200, height: 200, borderRadius: 8 }}
    />
  );
}
```

#### Props

| Prop    | Type                  | Default   | Description                                                    |
| ------- | --------------------- | --------- | -------------------------------------------------------------- |
| `color` | `string`              | `#000000` | Background color in hex format (e.g., "#FF5733")               |
| `style` | `StyleProp<ViewStyle>` | -         | Custom styles. Default dimensions are 100x100 if not specified |

### ModalBottomSheet

A Material 3 modal bottom sheet component with native gestures and animations.

```tsx
import { useState } from "react";
import { Button, Text, View } from "react-native";
import { ModalBottomSheet } from "@mgcrea/react-native-jetpack-compose";

function MyComponent() {
  const [visible, setVisible] = useState(false);

  return (
    <View style={{ flex: 1 }}>
      <Button title="Open Sheet" onPress={() => setVisible(true)} />

      <ModalBottomSheet visible={visible} onDismiss={() => setVisible(false)}>
        <View style={{ padding: 20 }}>
          <Text>Sheet Content</Text>
          <Button title="Close" onPress={() => setVisible(false)} />
        </View>
      </ModalBottomSheet>
    </View>
  );
}
```

#### Props

| Prop        | Type         | Default | Description                                        |
| ----------- | ------------ | ------- | -------------------------------------------------- |
| `visible`   | `boolean`    | `false` | Controls the visibility of the bottom sheet        |
| `onDismiss` | `() => void` | -       | Callback fired when the sheet is dismissed         |
| `children`  | `ReactNode`  | -       | Content to render inside the bottom sheet          |
| `style`     | `StyleProp<ViewStyle>` | -       | Custom styles (defaults to absolute fill) |

## Example

Check out the [example app](./example) for a complete demo:

```bash
cd example
npm install
npm run android
```

## Platform Support

| Platform | Supported |
| -------- | --------- |
| Android  | ✅        |
| iOS      | ❌        |

This library is Android-only as it uses Jetpack Compose under the hood.

## License

MIT © [Olivier Louvignes](https://github.com/mgcrea)
