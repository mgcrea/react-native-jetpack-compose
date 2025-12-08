---
title: TimePicker
description: Time selection component with dial or keyboard input
---

A time picker component that displays a text field which opens a Material 3 time picker dialog when tapped.

## Import

```tsx
import { TimePicker } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [time, setTime] = useState<Date | null>(null);

<TimePicker
  label="Select Time"
  value={time}
  onConfirm={setTime}
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `Date \| null` | `null` | Selected time (hours and minutes) |
| `is24Hour` | `boolean` | System default | Use 24-hour format |
| `initialDisplayMode` | `'picker' \| 'input'` | `'picker'` | Initial display mode (dial or keyboard) |
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
| `onConfirm` | `(time: Date \| null) => void` | User pressed OK |
| `onCancel` | `() => void` | User pressed Cancel |
| `onChange` | `(time: Date \| null) => void` | Time changed (before confirm) |

## Display Modes

### Picker Mode (Dial)

The default mode shows a clock dial for selecting hours and minutes:

```tsx
<TimePicker
  label="Time"
  value={time}
  onConfirm={setTime}
  initialDisplayMode="picker"
/>
```

### Input Mode (Keyboard)

Shows text fields for direct numeric input:

```tsx
<TimePicker
  label="Time"
  value={time}
  onConfirm={setTime}
  initialDisplayMode="input"
/>
```

## Examples

### Appointment Time

```tsx
const [appointmentTime, setAppointmentTime] = useState<Date | null>(null);

<TimePicker
  label="Appointment Time"
  value={appointmentTime}
  onConfirm={setAppointmentTime}
  title="Select appointment time"
/>
```

### 24-Hour Format

```tsx
const [time, setTime] = useState<Date | null>(null);

<TimePicker
  label="Time (24h)"
  value={time}
  onConfirm={setTime}
  is24Hour={true}
/>
```

### 12-Hour Format

```tsx
const [time, setTime] = useState<Date | null>(null);

<TimePicker
  label="Time (12h)"
  value={time}
  onConfirm={setTime}
  is24Hour={false}
/>
```

### Alarm Setting

```tsx
const [alarmTime, setAlarmTime] = useState<Date | null>(null);

<TimePicker
  label="Alarm"
  value={alarmTime}
  onConfirm={setAlarmTime}
  title="Set alarm time"
  placeholder="No alarm set"
/>

{alarmTime && (
  <Text>
    Alarm set for {alarmTime.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: '2-digit',
      hour12: true,
    })}
  </Text>
)}
```

### Meeting Scheduler

```tsx
const [meetingDate, setMeetingDate] = useState<Date | null>(null);
const [meetingTime, setMeetingTime] = useState<Date | null>(null);

// Combine date and time
const getMeetingDateTime = (): Date | null => {
  if (!meetingDate || !meetingTime) return null;

  const combined = new Date(meetingDate);
  combined.setHours(meetingTime.getHours());
  combined.setMinutes(meetingTime.getMinutes());
  return combined;
};

<DatePicker
  label="Meeting Date"
  value={meetingDate}
  onConfirm={setMeetingDate}
  minDate={new Date()}
/>
<TimePicker
  label="Meeting Time"
  value={meetingTime}
  onConfirm={setMeetingTime}
  disabled={!meetingDate}
/>
```

### Event Callbacks

```tsx
const [time, setTime] = useState<Date | null>(null);

<TimePicker
  label="Time"
  value={time}
  onChange={(t) => {
    // Called as user adjusts time
    console.log('Selecting:', t?.toLocaleTimeString());
  }}
  onConfirm={(t) => {
    // Called when user presses OK
    setTime(t);
    console.log('Confirmed:', t?.toLocaleTimeString());
  }}
  onCancel={() => {
    // Called when user presses Cancel
    console.log('Selection cancelled');
  }}
/>
```

### Working with Time Values

The component stores time in a Date object. To work with just hours and minutes:

```tsx
const [time, setTime] = useState<Date | null>(null);

// Get hours and minutes
const hours = time?.getHours() ?? 0;
const minutes = time?.getMinutes() ?? 0;

// Create time from hours/minutes
const setTimeFromHoursMinutes = (h: number, m: number) => {
  const d = new Date();
  d.setHours(h, m, 0, 0);
  setTime(d);
};

// Format for display
const formatTime = (t: Date | null): string => {
  if (!t) return '';
  return t.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
  });
};
```

## Related

- [TimeRangePicker](/react-native-jetpack-compose/components/time-range-picker/) - For selecting time ranges
- [DatePicker](/react-native-jetpack-compose/components/date-picker/) - For date selection
- [Date & Time Handling Guide](/react-native-jetpack-compose/guides/date-time-handling/) - Working with dates and times
