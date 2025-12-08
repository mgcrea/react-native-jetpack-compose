---
title: Date & Time Handling
description: Working with dates, times, and ranges in React Native Jetpack Compose
---

This guide covers best practices for handling dates and times with React Native Jetpack Compose components.

## Date Formats

### Accepted Input Formats

Date components accept multiple formats for `value`, `minDate`, `maxDate`, and `initialDisplayedMonth`:

```tsx
// JavaScript Date object
<DatePicker value={new Date(2024, 0, 15)} />

// Epoch milliseconds (number)
<DatePicker value={1705276800000} />

// ISO date string
<DatePicker value="2024-01-15" />

// ISO datetime string
<DatePicker value="2024-01-15T00:00:00.000Z" />
```

### Output Format

All `onConfirm` and `onChange` callbacks receive JavaScript `Date` objects:

```tsx
<DatePicker
  value={date}
  onConfirm={(selectedDate: Date | null) => {
    console.log(selectedDate);           // Date object
    console.log(selectedDate?.getTime()); // Epoch milliseconds
    console.log(selectedDate?.toISOString()); // ISO string
  }}
/>
```

## Working with Dates

### Date State Management

```tsx
// Nullable date (no initial selection)
const [date, setDate] = useState<Date | null>(null);

// With default value
const [date, setDate] = useState<Date | null>(new Date());

// With specific date
const [date, setDate] = useState<Date | null>(new Date(2024, 0, 15));
```

### Date Constraints

```tsx
// Past dates only
<DatePicker
  value={date}
  onConfirm={setDate}
  maxDate={new Date()}
/>

// Future dates only
<DatePicker
  value={date}
  onConfirm={setDate}
  minDate={new Date()}
/>

// Date range
const minDate = new Date(2024, 0, 1);  // Jan 1, 2024
const maxDate = new Date(2024, 11, 31); // Dec 31, 2024

<DatePicker
  value={date}
  onConfirm={setDate}
  minDate={minDate}
  maxDate={maxDate}
/>
```

### Year Range

Control the year selector:

```tsx
// Birth date picker
<DatePicker
  value={birthDate}
  onConfirm={setBirthDate}
  yearRange={{ start: 1920, end: new Date().getFullYear() }}
  maxDate={new Date()}
/>

// Appointment scheduler (next 2 years)
<DatePicker
  value={appointmentDate}
  onConfirm={setAppointmentDate}
  yearRange={{
    start: new Date().getFullYear(),
    end: new Date().getFullYear() + 2,
  }}
  minDate={new Date()}
/>
```

## Working with Times

### Time State Management

Time is stored in a `Date` object (only hours and minutes are relevant):

```tsx
const [time, setTime] = useState<Date | null>(null);

// Create time for specific hour/minute
const setTimeFromHoursMinutes = (hours: number, minutes: number) => {
  const d = new Date();
  d.setHours(hours, minutes, 0, 0);
  setTime(d);
};

// Extract hours and minutes
const hours = time?.getHours() ?? 0;
const minutes = time?.getMinutes() ?? 0;
```

### 12/24 Hour Format

```tsx
// Follow system setting (default)
<TimePicker value={time} onConfirm={setTime} />

// Force 24-hour format
<TimePicker value={time} onConfirm={setTime} is24Hour={true} />

// Force 12-hour format
<TimePicker value={time} onConfirm={setTime} is24Hour={false} />
```

## Working with Ranges

### Date Ranges

```tsx
import type { DateRange } from '@mgcrea/react-native-jetpack-compose';

const [dateRange, setDateRange] = useState<DateRange | null>(null);

// Access individual dates
const startDate = dateRange?.startDate;
const endDate = dateRange?.endDate;

// Calculate duration in days
const getDurationDays = (range: DateRange | null): number => {
  if (!range?.startDate || !range?.endDate) return 0;
  const ms = range.endDate.getTime() - range.startDate.getTime();
  return Math.ceil(ms / (1000 * 60 * 60 * 24));
};
```

### Time Ranges

```tsx
import type { TimeRange } from '@mgcrea/react-native-jetpack-compose';

const [timeRange, setTimeRange] = useState<TimeRange | null>(null);

// Access individual times
const startTime = timeRange?.startTime;
const endTime = timeRange?.endTime;

// Calculate duration in minutes
const getDurationMinutes = (range: TimeRange | null): number => {
  if (!range?.startTime || !range?.endTime) return 0;

  const startMinutes =
    range.startTime.getHours() * 60 + range.startTime.getMinutes();
  const endMinutes =
    range.endTime.getHours() * 60 + range.endTime.getMinutes();

  let duration = endMinutes - startMinutes;
  if (duration < 0) duration += 24 * 60; // Handle overnight

  return duration;
};
```

## Formatting Dates and Times

### Date Formatting

```tsx
const formatDate = (date: Date | null): string => {
  if (!date) return '';

  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
};

// "January 15, 2024"
```

### Time Formatting

```tsx
const formatTime = (time: Date | null): string => {
  if (!time) return '';

  return time.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  });
};

// "2:30 PM"
```

### Date Range Formatting

```tsx
const formatDateRange = (range: DateRange | null): string => {
  if (!range?.startDate || !range?.endDate) return '';

  const options: Intl.DateTimeFormatOptions = {
    month: 'short',
    day: 'numeric',
  };

  const start = range.startDate.toLocaleDateString('en-US', options);
  const end = range.endDate.toLocaleDateString('en-US', options);

  return `${start} - ${end}`;
};

// "Jan 15 - Jan 20"
```

### Time Range Formatting

```tsx
const formatTimeRange = (range: TimeRange | null): string => {
  if (!range?.startTime || !range?.endTime) return '';

  const options: Intl.DateTimeFormatOptions = {
    hour: 'numeric',
    minute: '2-digit',
  };

  const start = range.startTime.toLocaleTimeString('en-US', options);
  const end = range.endTime.toLocaleTimeString('en-US', options);

  return `${start} - ${end}`;
};

// "9:00 AM - 5:00 PM"
```

## Combining Date and Time

When you need both date and time selection:

```tsx
const [eventDate, setEventDate] = useState<Date | null>(null);
const [eventTime, setEventTime] = useState<Date | null>(null);

// Combine into single datetime
const getEventDateTime = (): Date | null => {
  if (!eventDate || !eventTime) return null;

  const combined = new Date(eventDate);
  combined.setHours(eventTime.getHours());
  combined.setMinutes(eventTime.getMinutes());
  combined.setSeconds(0);
  combined.setMilliseconds(0);

  return combined;
};

<DatePicker
  label="Event Date"
  value={eventDate}
  onConfirm={setEventDate}
/>
<TimePicker
  label="Event Time"
  value={eventTime}
  onConfirm={setEventTime}
  disabled={!eventDate}
/>
```

## Timezone Considerations

### Working with UTC

```tsx
// Convert local date to UTC for API
const toUTC = (date: Date): string => {
  return date.toISOString();
};

// Parse UTC from API to local Date
const fromUTC = (isoString: string): Date => {
  return new Date(isoString);
};
```

### Display in Specific Timezone

```tsx
const formatInTimezone = (date: Date, timezone: string): string => {
  return date.toLocaleString('en-US', {
    timeZone: timezone,
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
  });
};

// Display in different timezone
formatInTimezone(date, 'America/New_York');  // "Jan 15, 2024, 2:30 PM"
formatInTimezone(date, 'Europe/London');     // "Jan 15, 2024, 7:30 PM"
```

## Common Patterns

### Dependent Date Fields

```tsx
const [startDate, setStartDate] = useState<Date | null>(null);
const [endDate, setEndDate] = useState<Date | null>(null);

<DatePicker
  label="Start Date"
  value={startDate}
  onConfirm={(date) => {
    setStartDate(date);
    // Reset end date if it's before new start date
    if (endDate && date && endDate < date) {
      setEndDate(null);
    }
  }}
/>
<DatePicker
  label="End Date"
  value={endDate}
  onConfirm={setEndDate}
  minDate={startDate || undefined}
  disabled={!startDate}
/>
```

### Recurring Events

```tsx
const [startDate, setStartDate] = useState<Date | null>(null);
const [recurrence, setRecurrence] = useState<string | null>('none');
const [endDate, setEndDate] = useState<Date | null>(null);

<DatePicker
  label="Start Date"
  value={startDate}
  onConfirm={setStartDate}
/>
<Picker
  label="Repeat"
  value={recurrence}
  onChange={setRecurrence}
  options={[
    { value: 'none', label: 'Does not repeat' },
    { value: 'daily', label: 'Daily' },
    { value: 'weekly', label: 'Weekly' },
    { value: 'monthly', label: 'Monthly' },
  ]}
/>
{recurrence !== 'none' && (
  <DatePicker
    label="End Repeat"
    value={endDate}
    onConfirm={setEndDate}
    minDate={startDate || undefined}
  />
)}
```

### Business Hours

```tsx
interface DayHours {
  enabled: boolean;
  timeRange: TimeRange | null;
}

const [schedule, setSchedule] = useState<Record<string, DayHours>>({
  monday: { enabled: true, timeRange: null },
  tuesday: { enabled: true, timeRange: null },
  // ...
});

{Object.entries(schedule).map(([day, hours]) => (
  <View key={day}>
    <Text>{day}</Text>
    <TimeRangePicker
      label="Hours"
      value={hours.timeRange}
      onConfirm={(range) => {
        setSchedule(s => ({
          ...s,
          [day]: { ...s[day], timeRange: range },
        }));
      }}
      disabled={!hours.enabled}
      is24Hour={true}
    />
  </View>
))}
```
