---
title: SegmentedButton
description: Material 3 segmented button for single or multi-select choices
---

A segmented button component powered by Jetpack Compose's `SingleChoiceSegmentedButtonRow` and `MultiChoiceSegmentedButtonRow` with Material 3 styling.

## Import

```tsx
import { SegmentedButton } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [size, setSize] = useState<string | null>('m');

<SegmentedButton
  value={size}
  onChange={setSize}
  options={[
    { value: 's', label: 'S' },
    { value: 'm', label: 'M' },
    { value: 'l', label: 'L' },
  ]}
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `options` | `SegmentedButtonOption[]` | **Required** | Array of segment options |
| `value` | `string \| null` (single) or `string[]` (multi) | - | Currently selected value(s) |
| `multiSelect` | `boolean` | `false` | Enable multi-select mode |
| `disabled` | `boolean` | `false` | Disable the segmented button |
| `style` | `ViewStyle` | - | Container style |

## Events

| Event | Type | Description |
|-------|------|-------------|
| `onChange` | `(value: string) => void` (single) or `(values: string[]) => void` (multi) | Called when selection changes |

## Types

```tsx
interface SegmentedButtonOption {
  value: string;  // Unique identifier
  label: string;  // Display text
  icon?: string;  // Optional Material Icon name
}
```

### Available Icons

The following Material Icons are supported:

`check`, `star`, `favorite`, `home`, `settings`, `person`, `email`, `phone`, `search`, `add`, `close`, `done`, `edit`, `delete`, `share`, `info`, `warning`, `notifications`, `list`

## Examples

### Single Select (Default)

```tsx
const [alignment, setAlignment] = useState<string | null>('left');

<SegmentedButton
  value={alignment}
  onChange={setAlignment}
  options={[
    { value: 'left', label: 'Left' },
    { value: 'center', label: 'Center' },
    { value: 'right', label: 'Right' },
  ]}
/>
```

### Multi Select

```tsx
const [formats, setFormats] = useState<string[]>(['bold']);

<SegmentedButton
  multiSelect
  value={formats}
  onChange={setFormats}
  options={[
    { value: 'bold', label: 'B' },
    { value: 'italic', label: 'I' },
    { value: 'underline', label: 'U' },
  ]}
/>
```

### With Icons

```tsx
const [viewMode, setViewMode] = useState<string | null>('list');

<SegmentedButton
  value={viewMode}
  onChange={setViewMode}
  options={[
    { value: 'list', label: 'List', icon: 'list' },
    { value: 'grid', label: 'Grid', icon: 'star' },
  ]}
/>
```

### Disabled State

```tsx
<SegmentedButton
  disabled
  value="m"
  onChange={() => {}}
  options={[
    { value: 's', label: 'S' },
    { value: 'm', label: 'M' },
    { value: 'l', label: 'L' },
  ]}
/>
```

### Size Selector

```tsx
const [size, setSize] = useState<string | null>('m');

<SegmentedButton
  value={size}
  onChange={setSize}
  options={[
    { value: 'xs', label: 'XS' },
    { value: 's', label: 'S' },
    { value: 'm', label: 'M' },
    { value: 'l', label: 'L' },
    { value: 'xl', label: 'XL' },
  ]}
/>
```

### Filter Options

```tsx
const [filters, setFilters] = useState<string[]>([]);

<SegmentedButton
  multiSelect
  value={filters}
  onChange={setFilters}
  options={[
    { value: 'favorites', label: 'Favorites', icon: 'favorite' },
    { value: 'recent', label: 'Recent', icon: 'notifications' },
    { value: 'shared', label: 'Shared', icon: 'share' },
  ]}
/>
```

### Form Integration

```tsx
interface Settings {
  theme: string | null;
  notifications: string[];
}

const [settings, setSettings] = useState<Settings>({
  theme: 'system',
  notifications: ['email'],
});

<SegmentedButton
  value={settings.theme}
  onChange={(theme) => setSettings(s => ({ ...s, theme }))}
  options={[
    { value: 'light', label: 'Light' },
    { value: 'dark', label: 'Dark' },
    { value: 'system', label: 'System' },
  ]}
/>

<SegmentedButton
  multiSelect
  value={settings.notifications}
  onChange={(notifications) => setSettings(s => ({ ...s, notifications }))}
  options={[
    { value: 'email', label: 'Email', icon: 'email' },
    { value: 'push', label: 'Push', icon: 'notifications' },
    { value: 'sms', label: 'SMS', icon: 'phone' },
  ]}
/>
```

## When to Use

- **Few options (2-5)** - Ideal for quick visual selection
- **Binary choices** - Toggle between two states
- **Mode switching** - Switch between views (list/grid, day/week/month)
- **Multi-select filters** - Enable multiple toggleable options
- **Many options (6+)** - Consider [Picker](/react-native-jetpack-compose/components/picker/) or [SheetPicker](/react-native-jetpack-compose/components/sheet-picker/)
