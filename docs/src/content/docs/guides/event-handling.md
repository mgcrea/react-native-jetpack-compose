---
title: Event Handling
description: Understanding events and callbacks in React Native Jetpack Compose
---

This guide explains how events work in React Native Jetpack Compose components and best practices for handling them.

## Event Types

Components use two main event patterns:

### Immediate Events

These fire immediately when values change:

- `onChange` - Value changed
- `onFocus` - Component gained focus
- `onBlur` - Component lost focus

```tsx
<TextField
  value={text}
  onChange={(value) => setText(value)}   // Fires on every keystroke
  onFocus={() => console.log('Focused')}
  onBlur={() => console.log('Blurred')}
/>
```

### Confirmation Events

These fire when user explicitly confirms or cancels:

- `onConfirm` - User pressed OK/Confirm
- `onCancel` - User pressed Cancel or dismissed

```tsx
<DatePicker
  value={date}
  onConfirm={(value) => setDate(value)}  // User pressed OK
  onCancel={() => console.log('Cancelled')}
/>
```

## Component Event Patterns

### TextField

```tsx
<TextField
  value={text}
  onChange={(value: string) => {
    // Called on every keystroke
    setText(value);
  }}
  onFocus={() => {
    // Called when field gains focus
    setIsFocused(true);
  }}
  onBlur={() => {
    // Called when field loses focus
    setIsFocused(false);
    validateField();
  }}
  onSubmitEditing={() => {
    // Called when IME action button pressed (Done, Next, etc.)
    submitForm();
  }}
  onTrailingIconPress={() => {
    // Called when trailing icon tapped
    setText('');
  }}
/>
```

### Picker

```tsx
<Picker
  value={selected}
  onChange={(value: string) => {
    // Called when selection changes
    setSelected(value);
  }}
  options={options}
/>
```

### SheetPicker

```tsx
<SheetPicker
  value={selected}
  onSelect={(value: string) => {
    // Called when option selected
    setSelected(value);
  }}
  onDismiss={() => {
    // Called when sheet dismissed (regardless of selection)
    console.log('Sheet closed');
  }}
  options={options}
/>
```

### DatePicker / DateRangePicker

```tsx
<DatePicker
  value={date}
  onChange={(value: Date | null) => {
    // Called as user selects date (before confirmation)
    console.log('Selecting:', value);
  }}
  onConfirm={(value: Date | null) => {
    // Called when user presses OK
    setDate(value);
  }}
  onCancel={() => {
    // Called when user presses Cancel
    console.log('Selection cancelled');
  }}
/>
```

### TimePicker / TimeRangePicker

```tsx
<TimePicker
  value={time}
  onChange={(value: Date | null) => {
    // Called as user adjusts time (before confirmation)
    console.log('Selecting:', value?.toLocaleTimeString());
  }}
  onConfirm={(value: Date | null) => {
    // Called when user presses OK
    setTime(value);
  }}
  onCancel={() => {
    // Called when user presses Cancel
    console.log('Selection cancelled');
  }}
/>
```

### ModalBottomSheet

```tsx
<ModalBottomSheet
  visible={isVisible}
  onDismiss={() => {
    // Called when sheet is dismissed (swipe, tap outside, or programmatic)
    setIsVisible(false);
  }}
>
  {children}
</ModalBottomSheet>
```

## Best Practices

### Memoize Event Handlers

Prevent unnecessary re-renders by memoizing handlers:

```tsx
// Good: Handler reference is stable
const handleChange = useCallback((value: string) => {
  setText(value);
}, []);

<TextField value={text} onChange={handleChange} />

// Avoid: Creates new function on every render
<TextField value={text} onChange={(value) => setText(value)} />
```

### Handle Loading States

Disable interactions during async operations:

```tsx
const [isSubmitting, setIsSubmitting] = useState(false);

const handleConfirm = useCallback(async (date: Date | null) => {
  setIsSubmitting(true);
  try {
    await saveDate(date);
    setDate(date);
  } finally {
    setIsSubmitting(false);
  }
}, []);

<DatePicker
  value={date}
  onConfirm={handleConfirm}
  disabled={isSubmitting}
/>
```

### Validate on Events

Choose the right event for validation:

```tsx
// Real-time validation (as user types)
const handleChange = (value: string) => {
  setText(value);
  setError(value.length > 0 && !isValid(value));
};

// Validation on blur (when user leaves field)
const handleBlur = () => {
  if (!isValid(text)) {
    setError(true);
    setErrorMessage('Please enter a valid value');
  }
};

<TextField
  value={text}
  onChange={handleChange}
  onBlur={handleBlur}
  error={error}
  helperText={errorMessage}
/>
```

### Chain Dependent Updates

When one field affects another:

```tsx
const handleStartDateConfirm = useCallback((date: Date | null) => {
  setStartDate(date);

  // Clear end date if it's now invalid
  if (endDate && date && endDate < date) {
    setEndDate(null);
  }
}, [endDate]);

const handleEndDateConfirm = useCallback((date: Date | null) => {
  setEndDate(date);
}, []);

<DatePicker
  label="Start Date"
  value={startDate}
  onConfirm={handleStartDateConfirm}
/>
<DatePicker
  label="End Date"
  value={endDate}
  onConfirm={handleEndDateConfirm}
  minDate={startDate || undefined}
/>
```

## Event Flow Examples

### Form Submission Flow

```tsx
function ContactForm() {
  const [form, setForm] = useState({
    name: '',
    email: '',
    date: null as Date | null,
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const validate = () => {
    const newErrors: Record<string, string> = {};
    if (!form.name) newErrors.name = 'Name is required';
    if (!form.email.includes('@')) newErrors.email = 'Invalid email';
    if (!form.date) newErrors.date = 'Date is required';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) return;

    setIsSubmitting(true);
    try {
      await submitForm(form);
      Alert.alert('Success', 'Form submitted!');
    } catch (error) {
      Alert.alert('Error', 'Submission failed');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <View style={styles.form}>
      <TextField
        label="Name"
        value={form.name}
        onChange={(name) => {
          setForm(f => ({ ...f, name }));
          setErrors(e => ({ ...e, name: '' }));
        }}
        error={!!errors.name}
        helperText={errors.name}
        disabled={isSubmitting}
      />
      <TextField
        label="Email"
        value={form.email}
        onChange={(email) => {
          setForm(f => ({ ...f, email }));
          setErrors(e => ({ ...e, email: '' }));
        }}
        error={!!errors.email}
        helperText={errors.email}
        disabled={isSubmitting}
        keyboardType="email"
      />
      <DatePicker
        label="Date"
        value={form.date}
        onConfirm={(date) => {
          setForm(f => ({ ...f, date }));
          setErrors(e => ({ ...e, date: '' }));
        }}
        disabled={isSubmitting}
      />
      <Button
        title={isSubmitting ? 'Submitting...' : 'Submit'}
        onPress={handleSubmit}
        disabled={isSubmitting}
      />
    </View>
  );
}
```

### Multi-Step Selection

```tsx
function BookingFlow() {
  const [step, setStep] = useState(1);
  const [booking, setBooking] = useState({
    date: null as Date | null,
    time: null as Date | null,
    duration: null as string | null,
  });

  return (
    <View>
      {step === 1 && (
        <DatePicker
          label="Select Date"
          value={booking.date}
          onConfirm={(date) => {
            setBooking(b => ({ ...b, date }));
            setStep(2);
          }}
          onCancel={() => console.log('Cancelled')}
          minDate={new Date()}
        />
      )}

      {step === 2 && (
        <TimePicker
          label="Select Time"
          value={booking.time}
          onConfirm={(time) => {
            setBooking(b => ({ ...b, time }));
            setStep(3);
          }}
          onCancel={() => setStep(1)}
        />
      )}

      {step === 3 && (
        <Picker
          label="Duration"
          value={booking.duration}
          onChange={(duration) => {
            setBooking(b => ({ ...b, duration }));
            completeBooking(booking);
          }}
          options={[
            { value: '30', label: '30 minutes' },
            { value: '60', label: '1 hour' },
            { value: '90', label: '1.5 hours' },
          ]}
        />
      )}
    </View>
  );
}
```

## Debugging Events

### Log All Events

```tsx
<DatePicker
  value={date}
  onChange={(value) => {
    console.log('[DatePicker] onChange:', value);
  }}
  onConfirm={(value) => {
    console.log('[DatePicker] onConfirm:', value);
    setDate(value);
  }}
  onCancel={() => {
    console.log('[DatePicker] onCancel');
  }}
/>
```

### Track Event Timing

```tsx
const handleConfirm = useCallback((value: Date | null) => {
  const start = performance.now();
  setDate(value);
  const end = performance.now();
  console.log(`State update took ${end - start}ms`);
}, []);
```
