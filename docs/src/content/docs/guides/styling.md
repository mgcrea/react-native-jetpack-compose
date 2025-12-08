---
title: Styling
description: Styling components with Material 3 and React Native styles
---

This guide explains how styling works with React Native Jetpack Compose components.

## Material 3 Theming

All components use Material 3 (Material You) theming from the Android system. This means:

- **Dynamic colors**: On Android 12+, components adapt to the user's wallpaper colors
- **System theme**: Components follow the device's light/dark mode setting
- **Consistent design**: All components share the same design language

### What's Automatic

The following are handled automatically by Material 3:

- Component colors (primary, surface, outline, error)
- Typography (label text, helper text)
- Shapes (rounded corners, button shapes)
- Elevation and shadows
- State colors (focused, hovered, pressed, disabled)

### What You Can Control

Through the `style` prop, you can control:

- Container dimensions (width, height)
- Margins (spacing between components)
- Padding (on the container)
- Flex layout properties

## Using the style Prop

All components accept a `style` prop for container-level styling:

```tsx
import { StyleSheet } from 'react-native';

<TextField
  label="Name"
  value={name}
  onChange={setName}
  style={styles.input}
/>

const styles = StyleSheet.create({
  input: {
    marginBottom: 16,
    marginHorizontal: 8,
  },
});
```

### Common Style Properties

```tsx
// Margins
style={{ margin: 16 }}
style={{ marginTop: 8, marginBottom: 16 }}
style={{ marginHorizontal: 16, marginVertical: 8 }}

// Width
style={{ width: '100%' }}
style={{ width: 200 }}
style={{ maxWidth: 400 }}

// Flex
style={{ flex: 1 }}
style={{ alignSelf: 'stretch' }}
```

## Form Layout Patterns

### Vertical Form

```tsx
<View style={styles.form}>
  <TextField label="First Name" value={first} onChange={setFirst} />
  <TextField label="Last Name" value={last} onChange={setLast} />
  <TextField label="Email" value={email} onChange={setEmail} />
</View>

const styles = StyleSheet.create({
  form: {
    padding: 16,
    gap: 16, // Spacing between items
  },
});
```

### Two-Column Layout

```tsx
<View style={styles.form}>
  <View style={styles.row}>
    <TextField
      label="First Name"
      value={first}
      onChange={setFirst}
      style={styles.halfWidth}
    />
    <TextField
      label="Last Name"
      value={last}
      onChange={setLast}
      style={styles.halfWidth}
    />
  </View>
  <TextField label="Email" value={email} onChange={setEmail} />
</View>

const styles = StyleSheet.create({
  form: {
    padding: 16,
    gap: 16,
  },
  row: {
    flexDirection: 'row',
    gap: 16,
  },
  halfWidth: {
    flex: 1,
  },
});
```

### Card Layout

```tsx
<View style={styles.card}>
  <Text style={styles.cardTitle}>Contact Information</Text>
  <TextField label="Name" value={name} onChange={setName} />
  <TextField label="Email" value={email} onChange={setEmail} />
  <TextField label="Phone" value={phone} onChange={setPhone} />
</View>

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    margin: 16,
    gap: 12,
    // Shadow for Android
    elevation: 2,
  },
  cardTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 8,
  },
});
```

## TextField-Specific Styling

### Multiline Height

Control the minimum height for multiline text fields:

```tsx
<TextField
  label="Description"
  value={description}
  onChange={setDescription}
  multiline
  style={{ minHeight: 120 }}
/>
```

### Full-Width Input

```tsx
<TextField
  label="Search"
  value={search}
  onChange={setSearch}
  style={{ width: '100%' }}
/>
```

## Conditional Styling

### Error State

Use the `error` prop for validation styling:

```tsx
<TextField
  label="Email"
  value={email}
  onChange={setEmail}
  error={!isValidEmail(email)}
  helperText={!isValidEmail(email) ? 'Invalid email' : undefined}
/>
```

The error state automatically applies:
- Red outline color
- Red label color
- Red helper text color

### Disabled State

```tsx
<TextField
  label="Read Only"
  value={value}
  disabled
/>

<DatePicker
  label="Locked Date"
  value={date}
  disabled
/>
```

Disabled components automatically show:
- Reduced opacity
- No interaction
- Grayed colors

## Spacing Utilities

Create consistent spacing throughout your app:

```tsx
const spacing = {
  xs: 4,
  sm: 8,
  md: 16,
  lg: 24,
  xl: 32,
};

const styles = StyleSheet.create({
  form: {
    padding: spacing.md,
    gap: spacing.md,
  },
  section: {
    marginBottom: spacing.lg,
  },
  input: {
    marginBottom: spacing.sm,
  },
});
```

## Responsive Layouts

### Using Dimensions

```tsx
import { Dimensions, StyleSheet } from 'react-native';

const { width } = Dimensions.get('window');
const isTablet = width >= 768;

const styles = StyleSheet.create({
  form: {
    padding: isTablet ? 32 : 16,
    maxWidth: isTablet ? 600 : '100%',
    alignSelf: 'center',
  },
});
```

### Using useWindowDimensions

```tsx
import { useWindowDimensions, StyleSheet } from 'react-native';

function MyForm() {
  const { width } = useWindowDimensions();
  const isTablet = width >= 768;

  return (
    <View style={[styles.form, isTablet && styles.formTablet]}>
      {/* Form content */}
    </View>
  );
}

const styles = StyleSheet.create({
  form: {
    padding: 16,
  },
  formTablet: {
    padding: 32,
    maxWidth: 600,
    alignSelf: 'center',
  },
});
```

## Best Practices

### Do

```tsx
// Use gap for consistent spacing
<View style={{ gap: 16 }}>
  <TextField />
  <TextField />
</View>

// Use flex for equal-width inputs
<View style={{ flexDirection: 'row', gap: 16 }}>
  <TextField style={{ flex: 1 }} />
  <TextField style={{ flex: 1 }} />
</View>

// Use maxWidth for readability on large screens
<View style={{ maxWidth: 600, alignSelf: 'center' }}>
  <TextField />
</View>
```

### Avoid

```tsx
// Don't try to override Material 3 colors
<TextField style={{ backgroundColor: 'red' }} /> // Won't work

// Don't use fixed widths on responsive layouts
<TextField style={{ width: 300 }} /> // May overflow on small screens

// Don't mix margin approaches
<TextField style={{ margin: 16, marginBottom: 8 }} /> // Confusing
```

## Platform Considerations

These components are Android-only. For cross-platform forms, you might:

1. Use platform-specific components:

```tsx
import { Platform } from 'react-native';

const TextField = Platform.select({
  android: () => require('@mgcrea/react-native-jetpack-compose').TextField,
  ios: () => require('@mgcrea/react-native-swiftui').TextField,
  default: () => require('react-native').TextInput,
})();
```

2. Create wrapper components with consistent APIs across platforms
3. Use conditional rendering based on platform
