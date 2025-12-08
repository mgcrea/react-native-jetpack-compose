---
title: Installation
description: How to install and configure React Native Jetpack Compose
---

## Requirements

Before installing, ensure your project meets these requirements:

- **React Native**: 0.76.0 or later
- **New Architecture**: Must be enabled (Fabric)
- **Android**: API level 24 (Android 7.0) or higher
- **Node.js**: 18 or later

:::note[New Architecture Required]
This library uses React Native's Fabric renderer and will not work with the legacy Paper renderer. If you're starting a new project, New Architecture is enabled by default in React Native 0.76+.
:::

## Install the Package

Using npm:

```bash
npm install @mgcrea/react-native-jetpack-compose
```

Using yarn:

```bash
yarn add @mgcrea/react-native-jetpack-compose
```

Using pnpm:

```bash
pnpm add @mgcrea/react-native-jetpack-compose
```

## Android Configuration

The library automatically configures Jetpack Compose dependencies through autolinking. No manual Gradle configuration is required.

### Verify Compose is Enabled

In your `android/gradle.properties`, ensure these settings are present:

```properties
# Enable New Architecture
newArchEnabled=true

# Enable Jetpack Compose (usually set automatically)
# android.useAndroidX=true
```

### Minimum SDK Version

The library requires API level 24. In your `android/build.gradle` or `android/app/build.gradle`:

```groovy
android {
    defaultConfig {
        minSdkVersion 24
        // ...
    }
}
```

## Verify Installation

Create a simple test component to verify the installation:

```tsx
import { View, StyleSheet } from 'react-native';
import { TextField } from '@mgcrea/react-native-jetpack-compose';
import { useState } from 'react';

export function TestComponent() {
  const [text, setText] = useState('');

  return (
    <View style={styles.container}>
      <TextField
        label="Test Field"
        value={text}
        onChange={setText}
        placeholder="Type something..."
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 16,
  },
});
```

Build and run your app:

```bash
npx react-native run-android
```

If the text field renders with Material 3 styling, the installation is complete.

## Troubleshooting

### Build Errors

If you encounter build errors related to Compose or codegen:

1. Clean the build:
   ```bash
   cd android && ./gradlew clean && cd ..
   ```

2. Clear Metro cache:
   ```bash
   npx react-native start --reset-cache
   ```

3. Rebuild:
   ```bash
   npx react-native run-android
   ```

### Component Not Found

If you see errors like `ViewManager not found`:

1. Ensure the New Architecture is enabled in `gradle.properties`
2. Delete `android/build` and `android/app/build` folders
3. Run `npx react-native run-android` again

### Jetpack Compose Version Conflicts

If you have other libraries using different Compose versions, you can align versions in your `android/build.gradle`:

```groovy
ext {
    composeBomVersion = '2024.02.00'
}
```

## Next Steps

- [Quick Start](/react-native-jetpack-compose/getting-started/quick-start/) - Build your first form
- [Components](/react-native-jetpack-compose/components/) - Explore all available components
