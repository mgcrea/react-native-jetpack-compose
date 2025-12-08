---
title: Introduction
description: Material 3 components for React Native, powered by Jetpack Compose
---

**React Native Jetpack Compose** provides native Android UI components built directly on [Jetpack Compose](https://developer.android.com/jetpack/compose) and [Material 3](https://m3.material.io/). These components integrate seamlessly with React Native's New Architecture (Fabric) for optimal performance.

:::note[Android Only]
This library provides Android-only components. For iOS equivalents, see [@mgcrea/react-native-swiftui](https://github.com/mgcrea/react-native-swiftui).
:::

## Features

- **Native Performance**: Components render directly through Jetpack Compose, not via bridge calls
- **Material 3 Design**: Full Material You theming with dynamic colors on Android 12+
- **New Architecture**: Built for Fabric with codegen-generated type-safe bindings
- **TypeScript Support**: Complete type definitions for all components and props
- **Controlled Components**: Standard React patterns with `value`/`onChange` props

## Available Components

| Component | Description |
|-----------|-------------|
| [TextField](/react-native-jetpack-compose/components/text-field/) | Material 3 text input with labels, icons, and validation |
| [Picker](/react-native-jetpack-compose/components/picker/) | Dropdown picker for option selection |
| [SheetPicker](/react-native-jetpack-compose/components/sheet-picker/) | Searchable picker with bottom sheet modal |
| [DatePicker](/react-native-jetpack-compose/components/date-picker/) | Date selection with calendar dialog |
| [DateRangePicker](/react-native-jetpack-compose/components/date-range-picker/) | Date range selection |
| [TimePicker](/react-native-jetpack-compose/components/time-picker/) | Time selection with dial or keyboard input |
| [TimeRangePicker](/react-native-jetpack-compose/components/time-range-picker/) | Time range selection |
| [ModalBottomSheet](/react-native-jetpack-compose/components/modal-bottom-sheet/) | Draggable bottom sheet container |

## Requirements

- React Native 0.76.0 or later (New Architecture required)
- Android API level 24 (Android 7.0) or higher
- Jetpack Compose BOM 2024.02.00 or later (included)

## Quick Example

```tsx
import { useState } from 'react';
import { View } from 'react-native';
import { TextField, DatePicker } from '@mgcrea/react-native-jetpack-compose';

export function MyForm() {
  const [name, setName] = useState('');
  const [birthDate, setBirthDate] = useState<Date | null>(null);

  return (
    <View>
      <TextField
        label="Full Name"
        value={name}
        onChange={setName}
        placeholder="Enter your name"
      />
      <DatePicker
        label="Birth Date"
        value={birthDate}
        onConfirm={setBirthDate}
        maxDate={new Date()}
      />
    </View>
  );
}
```

## Next Steps

- [Installation](/react-native-jetpack-compose/getting-started/installation/) - Set up the library in your project
- [Quick Start](/react-native-jetpack-compose/getting-started/quick-start/) - Build your first form
- [How It Works](/react-native-jetpack-compose/getting-started/how-it-works/) - Understand the architecture
