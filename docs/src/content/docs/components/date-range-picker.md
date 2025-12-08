---
title: DateRangePicker
description: Date range selection component for start and end dates
---

A date range picker component that displays a text field which opens a Material 3 date range picker dialog when tapped.

## Import

```tsx
import { DateRangePicker } from '@mgcrea/react-native-jetpack-compose';
import type { DateRange } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [dateRange, setDateRange] = useState<DateRange | null>(null);

<DateRangePicker
  label="Select Dates"
  value={dateRange}
  onConfirm={setDateRange}
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `DateRange \| null` | `null` | Selected date range |
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
| `onConfirm` | `(range: DateRange) => void` | User pressed OK |
| `onCancel` | `() => void` | User pressed Cancel |
| `onChange` | `(range: DateRange) => void` | Range changed (before confirm) |

## Types

```tsx
interface DateRange {
  startDate: Date | null;
  endDate: Date | null;
}

interface YearRange {
  start: number;
  end: number;
}
```

## Examples

### Hotel Booking

```tsx
const [stayDates, setStayDates] = useState<DateRange | null>(null);

<DateRangePicker
  label="Stay Dates"
  value={stayDates}
  onConfirm={setStayDates}
  minDate={new Date()}
  title="Select check-in and check-out"
  placeholder="Select dates"
/>

{stayDates?.startDate && stayDates?.endDate && (
  <Text>
    {Math.ceil((stayDates.endDate.getTime() - stayDates.startDate.getTime()) / (1000 * 60 * 60 * 24))} nights
  </Text>
)}
```

### Vacation Planning

```tsx
const [vacation, setVacation] = useState<DateRange | null>(null);

// Allow planning up to 1 year in advance
const maxDate = new Date();
maxDate.setFullYear(maxDate.getFullYear() + 1);

<DateRangePicker
  label="Vacation Dates"
  value={vacation}
  onConfirm={setVacation}
  minDate={new Date()}
  maxDate={maxDate}
  title="Plan your vacation"
/>
```

### Report Date Range

```tsx
const [reportPeriod, setReportPeriod] = useState<DateRange | null>(null);

<DateRangePicker
  label="Report Period"
  value={reportPeriod}
  onConfirm={setReportPeriod}
  maxDate={new Date()}  // No future dates for reports
  title="Select report period"
/>
```

### With Validation

```tsx
const [dateRange, setDateRange] = useState<DateRange | null>(null);
const [error, setError] = useState<string | null>(null);

const handleConfirm = (range: DateRange) => {
  const { startDate, endDate } = range;

  if (startDate && endDate) {
    const days = Math.ceil(
      (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)
    );

    if (days > 14) {
      setError('Maximum booking is 14 days');
      return;
    }
  }

  setError(null);
  setDateRange(range);
};

<DateRangePicker
  label="Booking Dates"
  value={dateRange}
  onConfirm={handleConfirm}
/>
{error && <Text style={{ color: 'red' }}>{error}</Text>}
```

### Input Mode First

```tsx
<DateRangePicker
  label="Date Range"
  value={dateRange}
  onConfirm={setDateRange}
  initialDisplayMode="input"
/>
```

### Processing the Range

```tsx
const [dateRange, setDateRange] = useState<DateRange | null>(null);

const formatRange = (range: DateRange | null): string => {
  if (!range?.startDate || !range?.endDate) {
    return 'No dates selected';
  }

  const options: Intl.DateTimeFormatOptions = {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  };

  return `${range.startDate.toLocaleDateString('en-US', options)} - ${range.endDate.toLocaleDateString('en-US', options)}`;
};

<DateRangePicker
  label="Trip Dates"
  value={dateRange}
  onConfirm={setDateRange}
/>
<Text>{formatRange(dateRange)}</Text>
```

## Comparison with DatePicker

| Use Case | Component |
|----------|-----------|
| Single date (birthday, appointment) | [DatePicker](/react-native-jetpack-compose/components/date-picker/) |
| Date range (hotel stay, vacation, report period) | DateRangePicker |

## Related

- [DatePicker](/react-native-jetpack-compose/components/date-picker/) - For single date selection
- [Date & Time Handling Guide](/react-native-jetpack-compose/guides/date-time-handling/) - Working with dates
