# @mgcrea/react-native-jetpack-compose

[![npm version](https://img.shields.io/npm/v/@mgcrea/react-native-jetpack-compose.svg)](https://www.npmjs.com/package/@mgcrea/react-native-jetpack-compose)
[![license](https://img.shields.io/npm/l/@mgcrea/react-native-jetpack-compose.svg)](https://github.com/mgcrea/react-native-jetpack-compose/blob/main/LICENSE)

Material 3 components for React Native, powered by Jetpack Compose. This library provides native Android UI components with full Material Design 3 theming support.

## Features

- **DatePicker** - A text field that opens a Material 3 date picker dialog
- **DateRangePicker** - A text field that opens a date range picker dialog
- **Picker** - A dropdown picker using ExposedDropdownMenuBox
- **SheetPicker** - A searchable picker that opens in a modal bottom sheet
- **ModalBottomSheet** - A Material 3 modal bottom sheet with gesture support

## Requirements

- React Native 0.76+ (New Architecture required)
- Android SDK 24+ (minSdk)
- Android SDK 35+ (compileSdk)

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

### DatePicker

A text field that opens a Material 3 date picker dialog when tapped.

```tsx
import { useState } from "react";
import { DatePicker } from "@mgcrea/react-native-jetpack-compose";

function MyComponent() {
  const [date, setDate] = useState<Date | null>(null);

  return (
    <DatePicker
      value={date}
      label="Birth Date"
      placeholder="Select a date"
      onConfirm={(selectedDate) => setDate(selectedDate)}
    />
  );
}
```

#### Props

| Prop                 | Type                               | Default    | Description                                         |
| -------------------- | ---------------------------------- | ---------- | --------------------------------------------------- |
| `value`              | `Date \| number \| string \| null` | -          | Selected date (Date, epoch ms, or ISO string)       |
| `label`              | `string`                           | -          | Floating label text                                 |
| `placeholder`        | `string`                           | -          | Placeholder when no date selected                   |
| `disabled`           | `boolean`                          | `false`    | Whether the picker is disabled                      |
| `minDate`            | `Date \| number \| string`         | -          | Minimum selectable date                             |
| `maxDate`            | `Date \| number \| string`         | -          | Maximum selectable date                             |
| `yearRange`          | `{ start: number; end: number }`   | 1900-2100  | Year range for the year selector                    |
| `initialDisplayMode` | `"picker" \| "input"`              | `"picker"` | Initial mode (calendar or text input)               |
| `showModeToggle`     | `boolean`                          | `true`     | Show button to toggle between modes                 |
| `title`              | `string`                           | -          | Dialog title text                                   |
| `confirmLabel`       | `string`                           | `"OK"`     | Confirm button label                                |
| `cancelLabel`        | `string`                           | `"Cancel"` | Cancel button label                                 |
| `onConfirm`          | `(date: Date \| null) => void`     | -          | Called when user confirms selection                 |
| `onCancel`           | `() => void`                       | -          | Called when user cancels                            |
| `onChange`           | `(date: Date \| null) => void`     | -          | Called when selection changes (before confirmation) |

### DateRangePicker

A text field that opens a date range picker dialog for selecting start and end dates.

```tsx
import { useState } from "react";
import { DateRangePicker, type DateRange } from "@mgcrea/react-native-jetpack-compose";

function MyComponent() {
  const [range, setRange] = useState<DateRange>({ startDate: null, endDate: null });

  return (
    <DateRangePicker
      value={range}
      label="Trip Dates"
      placeholder="Select dates"
      onConfirm={(selectedRange) => setRange(selectedRange)}
    />
  );
}
```

#### Props

Same as DatePicker, with these differences:

| Prop        | Type                         | Description                      |
| ----------- | ---------------------------- | -------------------------------- |
| `value`     | `DateRange \| null`          | `{ startDate, endDate }` object  |
| `onConfirm` | `(range: DateRange) => void` | Called with selected range       |
| `onChange`  | `(range: DateRange) => void` | Called when range changes        |

### Picker

A dropdown picker component using Material 3's ExposedDropdownMenuBox.

```tsx
import { useState } from "react";
import { Picker } from "@mgcrea/react-native-jetpack-compose";

const countries = [
  { value: "us", label: "United States" },
  { value: "uk", label: "United Kingdom" },
  { value: "fr", label: "France" },
];

function MyComponent() {
  const [country, setCountry] = useState<string | null>(null);

  return (
    <Picker
      options={countries}
      value={country}
      label="Country"
      placeholder="Select a country"
      onChange={setCountry}
    />
  );
}
```

#### Props

| Prop          | Type                      | Default | Description                    |
| ------------- | ------------------------- | ------- | ------------------------------ |
| `options`     | `PickerOption[]`          | -       | Array of `{ value, label }`    |
| `value`       | `string \| null`          | -       | Currently selected value       |
| `label`       | `string`                  | -       | Floating label text            |
| `placeholder` | `string`                  | -       | Placeholder when no selection  |
| `disabled`    | `boolean`                 | `false` | Whether the picker is disabled |
| `onChange`    | `(value: string) => void` | -       | Called when selection changes  |

### SheetPicker

A searchable picker that opens a modal bottom sheet. Ideal for long lists.

```tsx
import { useState } from "react";
import { SheetPicker } from "@mgcrea/react-native-jetpack-compose";

const countries = [
  { value: "us", label: "United States" },
  { value: "uk", label: "United Kingdom" },
  // ... many more options
];

function MyComponent() {
  const [country, setCountry] = useState<string | null>(null);

  return (
    <SheetPicker
      options={countries}
      value={country}
      label="Country"
      placeholder="Select a country"
      title="Select Country"
      searchPlaceholder="Search countries..."
      onSelect={setCountry}
    />
  );
}
```

#### Props

| Prop                | Type                      | Default | Description                      |
| ------------------- | ------------------------- | ------- | -------------------------------- |
| `options`           | `SheetPickerOption[]`     | -       | Array of `{ value, label }`      |
| `value`             | `string \| null`          | -       | Currently selected value         |
| `label`             | `string`                  | -       | Floating label text              |
| `placeholder`       | `string`                  | -       | Placeholder when no selection    |
| `title`             | `string`                  | -       | Title at top of the sheet        |
| `searchPlaceholder` | `string`                  | -       | Placeholder for search field     |
| `autoDismiss`       | `boolean`                 | `true`  | Auto-close sheet after selection |
| `disabled`          | `boolean`                 | `false` | Whether the picker is disabled   |
| `onSelect`          | `(value: string) => void` | -       | Called when an option is selected|
| `onDismiss`         | `() => void`              | -       | Called when sheet is dismissed   |

### ModalBottomSheet

A Material 3 modal bottom sheet with native gestures and animations.

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

| Prop             | Type                   | Default | Description                         |
| ---------------- | ---------------------- | ------- | ----------------------------------- |
| `visible`        | `boolean`              | `false` | Controls visibility of the sheet    |
| `showDragHandle` | `boolean`              | `true`  | Show drag handle at top             |
| `maxHeightRatio` | `number`               | `0.9`   | Max height as ratio of screen (0-1) |
| `onDismiss`      | `() => void`           | -       | Called when sheet is dismissed      |
| `children`       | `ReactNode`            | -       | Content to render inside            |
| `style`          | `StyleProp<ViewStyle>` | -       | Custom styles                       |

## Example

Check out the [example app](./example) for a complete demo:

```bash
cd example
pnpm install
pnpm run android
```

## Platform Support

| Platform | Supported |
| -------- | --------- |
| Android  | ✅        |
| iOS      | ❌        |

This library is Android-only as it uses Jetpack Compose under the hood.

## License

MIT © [Olivier Louvignes](https://github.com/mgcrea)
