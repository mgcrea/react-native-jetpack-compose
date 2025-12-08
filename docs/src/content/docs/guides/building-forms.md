---
title: Building Forms
description: Patterns and best practices for building forms with React Native Jetpack Compose
---

This guide covers common patterns for building forms using React Native Jetpack Compose components.

## Basic Form Structure

A typical form combines multiple input components with state management:

```tsx
import { useState } from 'react';
import { View, Button, StyleSheet } from 'react-native';
import {
  TextField,
  Picker,
  DatePicker,
} from '@mgcrea/react-native-jetpack-compose';

export function BasicForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [category, setCategory] = useState<string | null>(null);
  const [date, setDate] = useState<Date | null>(null);

  const handleSubmit = () => {
    console.log({ name, email, category, date });
  };

  return (
    <View style={styles.form}>
      <TextField
        label="Name"
        value={name}
        onChange={setName}
      />
      <TextField
        label="Email"
        value={email}
        onChange={setEmail}
        keyboardType="email"
      />
      <Picker
        label="Category"
        value={category}
        onChange={setCategory}
        options={[
          { value: 'personal', label: 'Personal' },
          { value: 'business', label: 'Business' },
        ]}
      />
      <DatePicker
        label="Date"
        value={date}
        onConfirm={setDate}
      />
      <Button title="Submit" onPress={handleSubmit} />
    </View>
  );
}

const styles = StyleSheet.create({
  form: {
    padding: 16,
    gap: 16,
  },
});
```

## Form State Management

### Individual State

For simple forms, use individual `useState` hooks:

```tsx
const [firstName, setFirstName] = useState('');
const [lastName, setLastName] = useState('');
const [email, setEmail] = useState('');
```

### Object State

For forms with many fields, use a single state object:

```tsx
interface FormData {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  birthDate: Date | null;
}

const [form, setForm] = useState<FormData>({
  firstName: '',
  lastName: '',
  email: '',
  phone: '',
  birthDate: null,
});

const updateField = <K extends keyof FormData>(
  field: K,
  value: FormData[K]
) => {
  setForm(prev => ({ ...prev, [field]: value }));
};

// Usage
<TextField
  label="First Name"
  value={form.firstName}
  onChange={(value) => updateField('firstName', value)}
/>
```

### useReducer for Complex Forms

For forms with complex logic:

```tsx
type FormAction =
  | { type: 'SET_FIELD'; field: string; value: unknown }
  | { type: 'RESET' }
  | { type: 'SET_ERRORS'; errors: Record<string, string> };

function formReducer(state: FormState, action: FormAction): FormState {
  switch (action.type) {
    case 'SET_FIELD':
      return {
        ...state,
        data: { ...state.data, [action.field]: action.value },
        errors: { ...state.errors, [action.field]: undefined },
      };
    case 'SET_ERRORS':
      return { ...state, errors: action.errors };
    case 'RESET':
      return initialState;
    default:
      return state;
  }
}

const [state, dispatch] = useReducer(formReducer, initialState);
```

## Form Validation

### Field-Level Validation

Validate as the user types:

```tsx
const [email, setEmail] = useState('');
const [emailError, setEmailError] = useState<string | undefined>();

const validateEmail = (value: string) => {
  setEmail(value);
  if (value && !value.includes('@')) {
    setEmailError('Please enter a valid email');
  } else {
    setEmailError(undefined);
  }
};

<TextField
  label="Email"
  value={email}
  onChange={validateEmail}
  error={!!emailError}
  helperText={emailError}
  keyboardType="email"
/>
```

### Form-Level Validation

Validate on submit:

```tsx
interface Errors {
  name?: string;
  email?: string;
  date?: string;
}

const validate = (): Errors => {
  const errors: Errors = {};

  if (!name.trim()) {
    errors.name = 'Name is required';
  }

  if (!email.includes('@')) {
    errors.email = 'Valid email is required';
  }

  if (!date) {
    errors.date = 'Date is required';
  }

  return errors;
};

const handleSubmit = () => {
  const errors = validate();

  if (Object.keys(errors).length > 0) {
    setErrors(errors);
    return;
  }

  // Submit form
  submitForm({ name, email, date });
};
```

### Validation with Zod

Use a schema validation library:

```tsx
import { z } from 'zod';

const formSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  email: z.string().email('Invalid email address'),
  phone: z.string().regex(/^\d{10}$/, 'Phone must be 10 digits'),
  birthDate: z.date().max(new Date(), 'Birth date cannot be in the future'),
});

const handleSubmit = () => {
  const result = formSchema.safeParse({
    name,
    email,
    phone,
    birthDate,
  });

  if (!result.success) {
    const errors = result.error.flatten().fieldErrors;
    setErrors({
      name: errors.name?.[0],
      email: errors.email?.[0],
      phone: errors.phone?.[0],
      birthDate: errors.birthDate?.[0],
    });
    return;
  }

  submitForm(result.data);
};
```

## Form Patterns

### Registration Form

```tsx
export function RegistrationForm() {
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    birthDate: null as Date | null,
    country: null as string | null,
  });

  const [showPassword, setShowPassword] = useState(false);

  return (
    <View style={styles.form}>
      <TextField
        label="First Name"
        value={form.firstName}
        onChange={(v) => setForm(f => ({ ...f, firstName: v }))}
      />
      <TextField
        label="Last Name"
        value={form.lastName}
        onChange={(v) => setForm(f => ({ ...f, lastName: v }))}
      />
      <TextField
        label="Email"
        value={form.email}
        onChange={(v) => setForm(f => ({ ...f, email: v }))}
        keyboardType="email"
        autoCapitalize="none"
      />
      <TextField
        label="Password"
        value={form.password}
        onChange={(v) => setForm(f => ({ ...f, password: v }))}
        secureTextEntry={!showPassword}
        trailingIcon={showPassword ? "VisibilityOff" : "Visibility"}
        onTrailingIconPress={() => setShowPassword(!showPassword)}
      />
      <DatePicker
        label="Birth Date"
        value={form.birthDate}
        onConfirm={(v) => setForm(f => ({ ...f, birthDate: v }))}
        maxDate={new Date()}
      />
      <SheetPicker
        label="Country"
        value={form.country}
        onSelect={(v) => setForm(f => ({ ...f, country: v }))}
        options={countries}
      />
    </View>
  );
}
```

### Booking Form

```tsx
export function BookingForm() {
  const [form, setForm] = useState({
    guestName: '',
    email: '',
    checkIn: null as Date | null,
    checkOut: null as Date | null,
    roomType: null as string | null,
    guests: '1',
  });

  return (
    <View style={styles.form}>
      <TextField
        label="Guest Name"
        value={form.guestName}
        onChange={(v) => setForm(f => ({ ...f, guestName: v }))}
      />
      <TextField
        label="Email"
        value={form.email}
        onChange={(v) => setForm(f => ({ ...f, email: v }))}
        keyboardType="email"
      />
      <DatePicker
        label="Check-in"
        value={form.checkIn}
        onConfirm={(v) => setForm(f => ({ ...f, checkIn: v }))}
        minDate={new Date()}
      />
      <DatePicker
        label="Check-out"
        value={form.checkOut}
        onConfirm={(v) => setForm(f => ({ ...f, checkOut: v }))}
        minDate={form.checkIn || new Date()}
        disabled={!form.checkIn}
      />
      <Picker
        label="Room Type"
        value={form.roomType}
        onChange={(v) => setForm(f => ({ ...f, roomType: v }))}
        options={[
          { value: 'standard', label: 'Standard Room' },
          { value: 'deluxe', label: 'Deluxe Room' },
          { value: 'suite', label: 'Suite' },
        ]}
      />
      <Picker
        label="Number of Guests"
        value={form.guests}
        onChange={(v) => setForm(f => ({ ...f, guests: v }))}
        options={[
          { value: '1', label: '1 Guest' },
          { value: '2', label: '2 Guests' },
          { value: '3', label: '3 Guests' },
          { value: '4', label: '4 Guests' },
        ]}
      />
    </View>
  );
}
```

### Settings Form

```tsx
export function SettingsForm() {
  const [settings, setSettings] = useState({
    displayName: 'John Doe',
    email: 'john@example.com',
    language: 'en',
    timezone: 'America/New_York',
    notificationTime: null as Date | null,
  });

  return (
    <View style={styles.form}>
      <TextField
        label="Display Name"
        value={settings.displayName}
        onChange={(v) => setSettings(s => ({ ...s, displayName: v }))}
      />
      <TextField
        label="Email"
        value={settings.email}
        onChange={(v) => setSettings(s => ({ ...s, email: v }))}
        keyboardType="email"
      />
      <SheetPicker
        label="Language"
        value={settings.language}
        onSelect={(v) => setSettings(s => ({ ...s, language: v }))}
        title="Select Language"
        options={languages}
      />
      <SheetPicker
        label="Timezone"
        value={settings.timezone}
        onSelect={(v) => setSettings(s => ({ ...s, timezone: v }))}
        title="Select Timezone"
        options={timezones}
      />
      <TimePicker
        label="Daily Notification Time"
        value={settings.notificationTime}
        onConfirm={(v) => setSettings(s => ({ ...s, notificationTime: v }))}
      />
    </View>
  );
}
```

## Conditional Fields

Show fields based on other values:

```tsx
const [hasDeadline, setHasDeadline] = useState(false);
const [deadline, setDeadline] = useState<Date | null>(null);

<Picker
  label="Has Deadline?"
  value={hasDeadline ? 'yes' : 'no'}
  onChange={(v) => {
    setHasDeadline(v === 'yes');
    if (v === 'no') setDeadline(null);
  }}
  options={[
    { value: 'no', label: 'No' },
    { value: 'yes', label: 'Yes' },
  ]}
/>

{hasDeadline && (
  <DatePicker
    label="Deadline"
    value={deadline}
    onConfirm={setDeadline}
    minDate={new Date()}
  />
)}
```

## Loading and Disabled States

Disable form during submission:

```tsx
const [isSubmitting, setIsSubmitting] = useState(false);

const handleSubmit = async () => {
  setIsSubmitting(true);
  try {
    await submitForm(formData);
  } finally {
    setIsSubmitting(false);
  }
};

<TextField
  label="Name"
  value={name}
  onChange={setName}
  disabled={isSubmitting}
/>
<DatePicker
  label="Date"
  value={date}
  onConfirm={setDate}
  disabled={isSubmitting}
/>
<Button
  title={isSubmitting ? "Submitting..." : "Submit"}
  onPress={handleSubmit}
  disabled={isSubmitting}
/>
```
