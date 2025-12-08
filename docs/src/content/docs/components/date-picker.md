---
title: DatePicker
description: Date selection component with calendar dialog
---

A date picker component that displays a text field which opens a Material 3 date picker dialog when tapped.

## Import

```tsx
import { DatePicker } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [date, setDate] = useState<Date | null>(null);

<DatePicker
  label="Select Date"
  value={date}
  onConfirm={setDate}
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `Date \| number \| string \| null` | `null` | Selected date |
| `initialDisplayedMonth` | `Date \| number \| string` | - | Initially displayed month |
| `minDate` | `Date \| number \| string` | - | Minimum selectable date |
| `maxDate` | `Date \| number \| string` | - | Maximum selectable date |
| `yearRange` | `YearRange` | - | Year range for year selector |
| `initialDisplayMode` | `'picker' \| 'input'` | `'picker'` | Initial display mode |
| `showModeToggle` | `boolean` | `true` | Show mode toggle button |
| `confirmLabel` | `string` | `'OK'` | Confirm button text |
| `cancelLabel` | `string` | `'Cancel'` | Cancel button text |
| `title` | `string` | - | Dialog title |
| `label` | `string` | - | Text field label |
| `placeholder` | `string` | - | Text field placeholder |
| `disabled` | `boolean` | `false` | Disable the picker |
| `style` | `ViewStyle` | - | Container style |

## Events

| Event | Type | Description |
|-------|------|-------------|
| `onConfirm` | `(date: Date \| null) => void` | User pressed OK |
| `onCancel` | `() => void` | User pressed Cancel |
| `onChange` | `(date: Date \| null) => void` | Date changed (before confirm) |

## Types

```tsx
interface YearRange {
  start: number;  // First year to show
  end: number;    // Last year to show
}
```

## Date Value Formats

The `value`, `minDate`, `maxDate`, and `initialDisplayedMonth` props accept:

- `Date` object: `new Date(2024, 0, 15)`
- Epoch milliseconds: `1705276800000`
- ISO string: `'2024-01-15'` or `'2024-01-15T00:00:00.000Z'`

## Examples

### Birth Date Picker

```tsx
const [birthDate, setBirthDate] = useState<Date | null>(null);

<DatePicker
  label="Date of Birth"
  value={birthDate}
  onConfirm={setBirthDate}
  maxDate={new Date()}  // No future dates
  yearRange={{ start: 1920, end: new Date().getFullYear() }}
  title="Select your birth date"
/>
```

### Appointment Scheduler

```tsx
const [appointmentDate, setAppointmentDate] = useState<Date | null>(null);

// Allow booking 1-30 days in advance
const minDate = new Date();
minDate.setDate(minDate.getDate() + 1);

const maxDate = new Date();
maxDate.setDate(maxDate.getDate() + 30);

<DatePicker
  label="Appointment Date"
  value={appointmentDate}
  onConfirm={setAppointmentDate}
  minDate={minDate}
  maxDate={maxDate}
  title="Select appointment date"
/>
```

### With Event Handling

```tsx
const [date, setDate] = useState<Date | null>(null);

<DatePicker
  label="Event Date"
  value={date}
  onChange={(d) => console.log('Selecting:', d)}
  onConfirm={(d) => {
    setDate(d);
    console.log('Confirmed:', d);
  }}
  onCancel={() => console.log('Cancelled')}
/>
```

### Input Mode

Start in text input mode instead of calendar:

```tsx
<DatePicker
  label="Date"
  value={date}
  onConfirm={setDate}
  initialDisplayMode="input"
  showModeToggle={true}  // User can switch to calendar
/>
```

### Custom Labels

```tsx
<DatePicker
  label="Fecha"
  value={date}
  onConfirm={setDate}
  title="Seleccionar fecha"
  confirmLabel="Aceptar"
  cancelLabel="Cancelar"
  placeholder="Seleccione una fecha"
/>
```

### Controlled Display Month

Start the calendar on a specific month:

```tsx
<DatePicker
  label="Date"
  value={date}
  onConfirm={setDate}
  initialDisplayedMonth={new Date(2025, 0, 1)}  // January 2025
/>
```

### Form with Multiple Dates

```tsx
const [checkIn, setCheckIn] = useState<Date | null>(null);
const [checkOut, setCheckOut] = useState<Date | null>(null);

<DatePicker
  label="Check-in"
  value={checkIn}
  onConfirm={(d) => {
    setCheckIn(d);
    // Reset check-out if it's before new check-in
    if (checkOut && d && checkOut < d) {
      setCheckOut(null);
    }
  }}
  minDate={new Date()}
/>
<DatePicker
  label="Check-out"
  value={checkOut}
  onConfirm={setCheckOut}
  minDate={checkIn || new Date()}
  disabled={!checkIn}
/>
```

## Related

- [DateRangePicker](/react-native-jetpack-compose/components/date-range-picker/) - For selecting date ranges
- [Date & Time Handling Guide](/react-native-jetpack-compose/guides/date-time-handling/) - Working with dates
