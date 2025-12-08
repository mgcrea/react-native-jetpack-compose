---
title: Components Overview
description: Overview of all available React Native Jetpack Compose components
---

React Native Jetpack Compose provides 8 native Android components built on Jetpack Compose and Material 3.

## Form Inputs

| Component | Description |
|-----------|-------------|
| [TextField](/react-native-jetpack-compose/components/text-field/) | Material 3 text input with labels, icons, validation, and keyboard options |
| [Picker](/react-native-jetpack-compose/components/picker/) | Dropdown picker using ExposedDropdownMenuBox |
| [SheetPicker](/react-native-jetpack-compose/components/sheet-picker/) | Searchable picker with a modal bottom sheet |

## Date & Time

| Component | Description |
|-----------|-------------|
| [DatePicker](/react-native-jetpack-compose/components/date-picker/) | Date selection with calendar dialog |
| [DateRangePicker](/react-native-jetpack-compose/components/date-range-picker/) | Select start and end dates |
| [TimePicker](/react-native-jetpack-compose/components/time-picker/) | Time selection with dial or keyboard input |
| [TimeRangePicker](/react-native-jetpack-compose/components/time-range-picker/) | Select start and end times |

## Containers

| Component | Description |
|-----------|-------------|
| [ModalBottomSheet](/react-native-jetpack-compose/components/modal-bottom-sheet/) | Draggable bottom sheet for custom content |

## Common Patterns

### Importing Components

All components are exported from the main package:

```tsx
import {
  TextField,
  Picker,
  SheetPicker,
  DatePicker,
  DateRangePicker,
  TimePicker,
  TimeRangePicker,
  ModalBottomSheet,
} from '@mgcrea/react-native-jetpack-compose';
```

### Controlled Components

All input components follow the controlled component pattern:

```tsx
const [value, setValue] = useState('');

<TextField value={value} onChange={setValue} />
```

### Dialog Components

Date and time pickers show a dialog when tapped. They use a confirm/cancel pattern:

```tsx
const [date, setDate] = useState<Date | null>(null);

<DatePicker
  value={date}
  onConfirm={setDate}    // User pressed OK
  onCancel={() => {}}    // User pressed Cancel
  onChange={() => {}}    // Value changed (before confirm)
/>
```

### Styling

All components accept a `style` prop for container styling:

```tsx
<TextField
  label="Name"
  value={name}
  onChange={setName}
  style={{ marginBottom: 16, marginHorizontal: 8 }}
/>
```

Material 3 theming is applied automatically based on your Android system theme.

### Disabling Components

All components support a `disabled` prop:

```tsx
<TextField disabled label="Read Only" value="Cannot edit" />
<DatePicker disabled label="No Date Selection" />
<Picker disabled label="No Selection" options={[]} />
```
