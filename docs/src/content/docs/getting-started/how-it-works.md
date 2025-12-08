---
title: How It Works
description: Understanding the architecture of React Native Jetpack Compose
---

This guide explains how React Native Jetpack Compose integrates native Jetpack Compose components into your React Native app using the New Architecture.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                     React Native App                         │
├─────────────────────────────────────────────────────────────┤
│  TypeScript/JavaScript                                       │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  <DatePicker value={date} onConfirm={setDate} />    │    │
│  └─────────────────────────────────────────────────────┘    │
│                           │                                  │
│                     Fabric Bridge                            │
│                           │                                  │
├─────────────────────────────────────────────────────────────┤
│  Native Android (Kotlin)                                     │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  DatePickerView : InlineComposeView                  │    │
│  │    └── @Composable DatePicker()                      │    │
│  └─────────────────────────────────────────────────────┘    │
│                           │                                  │
│                   Jetpack Compose                            │
│                           │                                  │
│  ┌─────────────────────────────────────────────────────┐    │
│  │  Material 3 DatePickerDialog                         │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## Key Technologies

### React Native New Architecture (Fabric)

The library uses React Native's Fabric renderer, which provides:

- **Direct native calls**: No bridge serialization overhead
- **Synchronous operations**: Faster prop updates and event handling
- **Codegen**: Type-safe bindings generated from TypeScript specs

### Jetpack Compose

[Jetpack Compose](https://developer.android.com/jetpack/compose) is Android's modern declarative UI toolkit:

- **Kotlin-based**: Native Android development
- **Material 3**: Built-in Material Design 3 components
- **Dynamic theming**: Material You with dynamic colors on Android 12+

### Codegen

The library uses React Native's codegen to generate native bindings:

1. TypeScript specs define props and events
2. Codegen generates Java interfaces and C++ descriptors
3. Kotlin implementations fulfill these interfaces

## Component Structure

Each component follows a consistent pattern:

### 1. TypeScript Spec

Defines the native component interface:

```tsx
// DatePickerNativeComponent.ts
import type { ViewProps } from 'react-native';
import type { DirectEventHandler, Double } from 'react-native/Libraries/Types/CodegenTypes';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';

interface NativeProps extends ViewProps {
  value?: Double;
  minDate?: Double;
  maxDate?: Double;
  onConfirm?: DirectEventHandler<{ value: Double }>;
}

export default codegenNativeComponent<NativeProps>('DatePicker');
```

### 2. TypeScript Wrapper

Provides a user-friendly API:

```tsx
// DatePicker.tsx
export function DatePicker({ value, onConfirm, ...props }: DatePickerProps) {
  // Convert Date to epoch milliseconds for native
  const nativeValue = value?.getTime();

  // Convert native epoch back to Date
  const handleConfirm = (event: { nativeEvent: { value: number } }) => {
    onConfirm?.(new Date(event.nativeEvent.value));
  };

  return (
    <DatePickerNative
      value={nativeValue}
      onConfirm={handleConfirm}
      {...props}
    />
  );
}
```

### 3. ViewManager (Kotlin)

Implements the codegen-generated interface:

```kotlin
// DatePickerViewManager.kt
class DatePickerViewManager : SimpleViewManager<DatePickerView>(),
    DatePickerViewManagerInterface<DatePickerView> {

    override fun getName() = "DatePicker"

    override fun createViewInstance(context: ThemedReactContext) =
        DatePickerView(context)

    override fun setValue(view: DatePickerView, value: Double?) {
        view.setValue(value?.toLong())
    }

    // ... more prop setters
}
```

### 4. View Implementation (Kotlin)

The actual Compose-based view:

```kotlin
// DatePickerView.kt
class DatePickerView(context: Context) : InlineComposeView(context) {
    private var selectedDate: Long? by mutableStateOf(null)

    fun setValue(value: Long?) {
        selectedDate = value
    }

    @Composable
    override fun Content() {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )

        DatePickerDialog(
            onConfirm = {
                dispatchEvent(ConfirmEvent(id, datePickerState.selectedDateMillis))
            },
            // ...
        )
    }
}
```

## Event Flow

Events flow from native to JavaScript:

1. User interacts with Compose UI (e.g., selects a date)
2. Compose callback fires in Kotlin
3. Event dispatched via `dispatchEvent()`
4. Fabric delivers event to JavaScript
5. TypeScript wrapper transforms and calls `onConfirm`

```kotlin
// Native side
dispatchEvent(ConfirmEvent(viewId, dateMillis))

// Event class
class ConfirmEvent(viewId: Int, private val value: Long) :
    Event<ConfirmEvent>(viewId) {
    override fun getEventName() = "topConfirm"
    override fun getEventData() = mapOf("value" to value.toDouble())
}
```

```tsx
// JavaScript side receives
{ nativeEvent: { value: 1705276800000 } }
```

## Prop Updates

Props flow from JavaScript to native:

1. React state changes (e.g., `setValue(newDate)`)
2. Fabric detects prop change
3. ViewManager setter called (e.g., `setValue()`)
4. Compose state updated, triggering recomposition

```tsx
// JavaScript
<DatePicker value={date} />
```

```kotlin
// Native - ViewManager
override fun setValue(view: DatePickerView, value: Double?) {
    view.setValue(value?.toLong())
}

// Native - View (triggers recomposition)
fun setValue(value: Long?) {
    selectedDate = value  // MutableState
}
```

## Styling Integration

Components use Material 3 theming from the Android system:

- Colors: Dynamic colors on Android 12+, Material defaults otherwise
- Typography: System Material 3 type scale
- Shapes: Material 3 shape system

React Native `style` props control:
- Container dimensions and positioning
- Margins and padding (via container)

```tsx
<DatePicker
  style={{
    marginBottom: 16,    // Applied to container
    marginHorizontal: 8,
  }}
/>
```

## Performance Considerations

### Why Native Components?

- **No JS thread blocking**: UI renders on native thread
- **Native animations**: 60fps animations without JS bridge
- **Platform consistency**: Identical to native Android apps

### Best Practices

1. **Minimize prop updates**: Avoid unnecessary re-renders
2. **Use callbacks properly**: Memoize handlers with `useCallback`
3. **Batch state updates**: Combine related state changes

```tsx
// Good: Memoized callback
const handleConfirm = useCallback((date: Date | null) => {
  setDate(date);
}, []);

// Avoid: Inline function creates new reference each render
<DatePicker onConfirm={(date) => setDate(date)} />
```

## Debugging

### View Hierarchy

Use Android Studio's Layout Inspector to see:
- React Native view tree
- Compose component tree
- Actual rendered UI

### Logging

Enable detailed logs:

```bash
adb logcat -s ReactNative:V ReactNativeJS:V
```

### Common Issues

1. **Component not rendering**: Check New Architecture is enabled
2. **Props not updating**: Verify ViewManager implements setter
3. **Events not firing**: Check event name matches (`top` + PascalCase)
