---
title: Quick Start
description: Build your first form with React Native Jetpack Compose
---

This guide walks you through building a simple contact form using React Native Jetpack Compose components.

## Basic Setup

Import the components you need:

```tsx
import {
  TextField,
  Picker,
  DatePicker,
} from '@mgcrea/react-native-jetpack-compose';
```

## Building a Contact Form

Here's a complete example of a contact form:

```tsx
import { useState } from 'react';
import { View, Button, StyleSheet, Alert } from 'react-native';
import { TextField, Picker, DatePicker } from '@mgcrea/react-native-jetpack-compose';

export function ContactForm() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [subject, setSubject] = useState<string | null>(null);
  const [followUpDate, setFollowUpDate] = useState<Date | null>(null);
  const [message, setMessage] = useState('');

  const handleSubmit = () => {
    Alert.alert('Form Submitted', `Name: ${name}\nEmail: ${email}`);
  };

  return (
    <View style={styles.container}>
      <TextField
        label="Name"
        value={name}
        onChange={setName}
        placeholder="Enter your name"
      />

      <TextField
        label="Email"
        value={email}
        onChange={setEmail}
        placeholder="Enter your email"
        keyboardType="email"
      />

      <Picker
        label="Subject"
        value={subject}
        onChange={setSubject}
        placeholder="Select a subject"
        options={[
          { value: 'general', label: 'General Inquiry' },
          { value: 'support', label: 'Technical Support' },
          { value: 'billing', label: 'Billing Question' },
          { value: 'feedback', label: 'Feedback' },
        ]}
      />

      <DatePicker
        label="Follow-up Date"
        value={followUpDate}
        onConfirm={setFollowUpDate}
        placeholder="Select a date"
        minDate={new Date()}
      />

      <TextField
        label="Message"
        value={message}
        onChange={setMessage}
        placeholder="Enter your message"
        multiline
      />

      <Button title="Submit" onPress={handleSubmit} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 16,
    gap: 16,
  },
});
```

## Key Concepts

### Controlled Components

All components follow React's controlled component pattern:

```tsx
const [value, setValue] = useState('');

<TextField
  value={value}        // Current value
  onChange={setValue}  // Called when value changes
/>
```

### Event Handling

Different components have different event patterns:

- **TextField**: `onChange` fires on every keystroke
- **Picker**: `onChange` fires when selection changes
- **DatePicker/TimePicker**: `onConfirm` fires when user confirms, `onChange` fires during selection

```tsx
<DatePicker
  value={date}
  onChange={(d) => console.log('Selecting:', d)}  // During selection
  onConfirm={(d) => setDate(d)}                   // On confirm
  onCancel={() => console.log('Cancelled')}       // On cancel
/>
```

### Styling

Components accept a `style` prop for container styling:

```tsx
<TextField
  label="Name"
  value={name}
  onChange={setName}
  style={{ marginBottom: 16 }}
/>
```

Material 3 theming is handled automatically based on your Android system theme.

## Adding Validation

Use the `error` and `helperText` props for validation feedback:

```tsx
const [email, setEmail] = useState('');
const [emailError, setEmailError] = useState(false);

const validateEmail = (text: string) => {
  setEmail(text);
  setEmailError(text.length > 0 && !text.includes('@'));
};

<TextField
  label="Email"
  value={email}
  onChange={validateEmail}
  error={emailError}
  helperText={emailError ? 'Please enter a valid email' : undefined}
  keyboardType="email"
/>
```

## Using Icons

TextField supports Material Icons for leading and trailing icons:

```tsx
<TextField
  label="Search"
  value={search}
  onChange={setSearch}
  leadingIcon="Search"
  trailingIcon={search ? "Clear" : undefined}
  onTrailingIconPress={() => setSearch('')}
/>
```

## Next Steps

- [Components](/react-native-jetpack-compose/components/) - Explore all components and their props
- [Building Forms](/react-native-jetpack-compose/guides/building-forms/) - Advanced form patterns
- [Event Handling](/react-native-jetpack-compose/guides/event-handling/) - Deep dive into events
