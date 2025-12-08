---
title: TimeRangePicker
description: Time range selection component for start and end times
---

A time range picker component that displays a text field which opens a dialog for selecting start and end times.

## Import

```tsx
import { TimeRangePicker } from '@mgcrea/react-native-jetpack-compose';
import type { TimeRange } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [timeRange, setTimeRange] = useState<TimeRange | null>(null);

<TimeRangePicker
  label="Select Time Range"
  value={timeRange}
  onConfirm={setTimeRange}
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `TimeRange \| null` | `null` | Selected time range |
| `is24Hour` | `boolean` | System default | Use 24-hour format |
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
| `onConfirm` | `(range: TimeRange) => void` | User confirmed selection |
| `onCancel` | `() => void` | User cancelled |
| `onChange` | `(range: TimeRange) => void` | Range changed (before confirm) |

## Types

```tsx
interface TimeRange {
  startTime: Date | null;
  endTime: Date | null;
}
```

## Examples

### Business Hours

```tsx
const [businessHours, setBusinessHours] = useState<TimeRange | null>(null);

<TimeRangePicker
  label="Business Hours"
  value={businessHours}
  onConfirm={setBusinessHours}
  title="Set business hours"
  is24Hour={true}
/>
```

### Meeting Duration

```tsx
const [meetingTime, setMeetingTime] = useState<TimeRange | null>(null);

// Calculate duration
const getDuration = (range: TimeRange | null): string => {
  if (!range?.startTime || !range?.endTime) return '';

  const startMinutes = range.startTime.getHours() * 60 + range.startTime.getMinutes();
  const endMinutes = range.endTime.getHours() * 60 + range.endTime.getMinutes();
  let durationMinutes = endMinutes - startMinutes;

  // Handle overnight ranges
  if (durationMinutes < 0) {
    durationMinutes += 24 * 60;
  }

  const hours = Math.floor(durationMinutes / 60);
  const minutes = durationMinutes % 60;

  if (hours > 0 && minutes > 0) {
    return `${hours}h ${minutes}m`;
  } else if (hours > 0) {
    return `${hours}h`;
  } else {
    return `${minutes}m`;
  }
};

<TimeRangePicker
  label="Meeting Time"
  value={meetingTime}
  onConfirm={setMeetingTime}
/>
<Text>Duration: {getDuration(meetingTime)}</Text>
```

### Shift Scheduling

```tsx
const [shift, setShift] = useState<TimeRange | null>(null);

<TimeRangePicker
  label="Shift Hours"
  value={shift}
  onConfirm={setShift}
  title="Set shift schedule"
  is24Hour={true}
/>
```

### Availability Window

```tsx
const [availability, setAvailability] = useState<TimeRange | null>(null);

<TimeRangePicker
  label="Available Hours"
  value={availability}
  onConfirm={setAvailability}
  title="When are you available?"
  placeholder="Set your availability"
/>
```

### Event Scheduling with Date

```tsx
const [eventDate, setEventDate] = useState<Date | null>(null);
const [eventTime, setEventTime] = useState<TimeRange | null>(null);

<DatePicker
  label="Event Date"
  value={eventDate}
  onConfirm={setEventDate}
  minDate={new Date()}
/>
<TimeRangePicker
  label="Event Time"
  value={eventTime}
  onConfirm={setEventTime}
  disabled={!eventDate}
/>
```

### Formatting Time Range

```tsx
const [timeRange, setTimeRange] = useState<TimeRange | null>(null);

const formatTimeRange = (range: TimeRange | null): string => {
  if (!range?.startTime || !range?.endTime) {
    return 'Not set';
  }

  const options: Intl.DateTimeFormatOptions = {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  };

  const start = range.startTime.toLocaleTimeString('en-US', options);
  const end = range.endTime.toLocaleTimeString('en-US', options);

  return `${start} - ${end}`;
};

<TimeRangePicker
  label="Hours"
  value={timeRange}
  onConfirm={setTimeRange}
/>
<Text>{formatTimeRange(timeRange)}</Text>
```

### Multiple Time Ranges (Schedule)

```tsx
interface DaySchedule {
  day: string;
  timeRange: TimeRange | null;
}

const [schedule, setSchedule] = useState<DaySchedule[]>([
  { day: 'Monday', timeRange: null },
  { day: 'Tuesday', timeRange: null },
  { day: 'Wednesday', timeRange: null },
  { day: 'Thursday', timeRange: null },
  { day: 'Friday', timeRange: null },
]);

{schedule.map((item, index) => (
  <TimeRangePicker
    key={item.day}
    label={item.day}
    value={item.timeRange}
    onConfirm={(range) => {
      const newSchedule = [...schedule];
      newSchedule[index].timeRange = range;
      setSchedule(newSchedule);
    }}
    is24Hour={true}
  />
))}
```

## Comparison with TimePicker

| Use Case | Component |
|----------|-----------|
| Single time (alarm, appointment start) | [TimePicker](/react-native-jetpack-compose/components/time-picker/) |
| Time range (business hours, shifts, events) | TimeRangePicker |

## Related

- [TimePicker](/react-native-jetpack-compose/components/time-picker/) - For single time selection
- [DateRangePicker](/react-native-jetpack-compose/components/date-range-picker/) - For date range selection
- [Date & Time Handling Guide](/react-native-jetpack-compose/guides/date-time-handling/) - Working with dates and times
