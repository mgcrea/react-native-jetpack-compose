---
title: Picker
description: Dropdown picker component for selecting from a list of options
---

A dropdown picker component powered by Jetpack Compose's `ExposedDropdownMenuBox` with Material 3 styling.

## Import

```tsx
import { Picker } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [country, setCountry] = useState<string | null>(null);

<Picker
  label="Country"
  value={country}
  onChange={setCountry}
  options={[
    { value: 'us', label: 'United States' },
    { value: 'uk', label: 'United Kingdom' },
    { value: 'ca', label: 'Canada' },
  ]}
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `options` | `PickerOption[]` | **Required** | Array of selectable options |
| `value` | `string \| null` | `null` | Currently selected value |
| `label` | `string` | - | Floating label text |
| `placeholder` | `string` | - | Placeholder when no selection |
| `disabled` | `boolean` | `false` | Disable the picker |
| `style` | `ViewStyle` | - | Container style |

## Events

| Event | Type | Description |
|-------|------|-------------|
| `onChange` | `(value: string) => void` | Called when selection changes |

## Types

```tsx
interface PickerOption {
  value: string;  // Unique identifier
  label: string;  // Display text
}
```

## Examples

### Basic Selection

```tsx
const [priority, setPriority] = useState<string | null>(null);

<Picker
  label="Priority"
  value={priority}
  onChange={setPriority}
  placeholder="Select priority"
  options={[
    { value: 'low', label: 'Low' },
    { value: 'medium', label: 'Medium' },
    { value: 'high', label: 'High' },
    { value: 'urgent', label: 'Urgent' },
  ]}
/>
```

### With Default Value

```tsx
const [status, setStatus] = useState('active');

<Picker
  label="Status"
  value={status}
  onChange={setStatus}
  options={[
    { value: 'active', label: 'Active' },
    { value: 'inactive', label: 'Inactive' },
    { value: 'pending', label: 'Pending' },
  ]}
/>
```

### Dynamic Options

```tsx
const [category, setCategory] = useState<string | null>(null);

const categories = useMemo(() =>
  data.map(item => ({
    value: item.id,
    label: item.name,
  })),
  [data]
);

<Picker
  label="Category"
  value={category}
  onChange={setCategory}
  options={categories}
/>
```

### Disabled State

```tsx
<Picker
  label="Locked Selection"
  value="option1"
  onChange={() => {}}
  disabled
  options={[
    { value: 'option1', label: 'Option 1' },
    { value: 'option2', label: 'Option 2' },
  ]}
/>
```

### Form Integration

```tsx
interface FormData {
  name: string;
  department: string | null;
  role: string | null;
}

const [form, setForm] = useState<FormData>({
  name: '',
  department: null,
  role: null,
});

<TextField
  label="Name"
  value={form.name}
  onChange={(name) => setForm(f => ({ ...f, name }))}
/>
<Picker
  label="Department"
  value={form.department}
  onChange={(department) => setForm(f => ({ ...f, department }))}
  options={[
    { value: 'engineering', label: 'Engineering' },
    { value: 'design', label: 'Design' },
    { value: 'marketing', label: 'Marketing' },
    { value: 'sales', label: 'Sales' },
  ]}
/>
<Picker
  label="Role"
  value={form.role}
  onChange={(role) => setForm(f => ({ ...f, role }))}
  options={[
    { value: 'member', label: 'Team Member' },
    { value: 'lead', label: 'Team Lead' },
    { value: 'manager', label: 'Manager' },
  ]}
/>
```

## When to Use

- **Short lists** (under 10 items) - Use Picker for quick selection
- **Long lists** (10+ items) - Consider [SheetPicker](/react-native-jetpack-compose/components/sheet-picker/) for searchability
- **Many options with search** - Use SheetPicker for better UX
