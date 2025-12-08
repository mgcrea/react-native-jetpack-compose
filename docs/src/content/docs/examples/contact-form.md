---
title: Contact Form
description: A complete contact form example with validation
---

A practical contact form demonstrating TextField, Picker, and validation patterns.

## Complete Example

```tsx
import { useState, useCallback } from 'react';
import { View, Button, Text, Alert, StyleSheet, ScrollView } from 'react-native';
import {
  TextField,
  Picker,
  DatePicker,
} from '@mgcrea/react-native-jetpack-compose';

interface FormData {
  name: string;
  email: string;
  phone: string;
  subject: string | null;
  preferredContact: string | null;
  preferredDate: Date | null;
  message: string;
}

interface FormErrors {
  name?: string;
  email?: string;
  phone?: string;
  subject?: string;
  message?: string;
}

const subjectOptions = [
  { value: 'general', label: 'General Inquiry' },
  { value: 'support', label: 'Technical Support' },
  { value: 'billing', label: 'Billing Question' },
  { value: 'partnership', label: 'Partnership Opportunity' },
  { value: 'feedback', label: 'Feedback' },
];

const contactMethodOptions = [
  { value: 'email', label: 'Email' },
  { value: 'phone', label: 'Phone' },
  { value: 'either', label: 'Either' },
];

export function ContactForm() {
  const [form, setForm] = useState<FormData>({
    name: '',
    email: '',
    phone: '',
    subject: null,
    preferredContact: 'email',
    preferredDate: null,
    message: '',
  });

  const [errors, setErrors] = useState<FormErrors>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const updateField = useCallback(<K extends keyof FormData>(
    field: K,
    value: FormData[K]
  ) => {
    setForm(prev => ({ ...prev, [field]: value }));
    // Clear error when user starts typing
    if (errors[field as keyof FormErrors]) {
      setErrors(prev => ({ ...prev, [field]: undefined }));
    }
  }, [errors]);

  const validateEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const validatePhone = (phone: string): boolean => {
    const phoneRegex = /^\d{10}$/;
    return phone === '' || phoneRegex.test(phone.replace(/\D/g, ''));
  };

  const validate = (): boolean => {
    const newErrors: FormErrors = {};

    if (!form.name.trim()) {
      newErrors.name = 'Name is required';
    }

    if (!form.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!validateEmail(form.email)) {
      newErrors.email = 'Please enter a valid email address';
    }

    if (form.phone && !validatePhone(form.phone)) {
      newErrors.phone = 'Please enter a valid 10-digit phone number';
    }

    if (!form.subject) {
      newErrors.subject = 'Please select a subject';
    }

    if (!form.message.trim()) {
      newErrors.message = 'Message is required';
    } else if (form.message.trim().length < 10) {
      newErrors.message = 'Message must be at least 10 characters';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) {
      return;
    }

    setIsSubmitting(true);

    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1500));

      Alert.alert(
        'Success',
        'Thank you for your message! We will get back to you soon.',
        [{ text: 'OK', onPress: resetForm }]
      );
    } catch (error) {
      Alert.alert('Error', 'Failed to send message. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const resetForm = () => {
    setForm({
      name: '',
      email: '',
      phone: '',
      subject: null,
      preferredContact: 'email',
      preferredDate: null,
      message: '',
    });
    setErrors({});
  };

  // Calculate minimum date (tomorrow)
  const minDate = new Date();
  minDate.setDate(minDate.getDate() + 1);

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Contact Us</Text>
      <Text style={styles.subtitle}>
        Fill out the form below and we'll get back to you as soon as possible.
      </Text>

      <View style={styles.form}>
        <TextField
          label="Full Name"
          value={form.name}
          onChange={(value) => updateField('name', value)}
          placeholder="Enter your name"
          error={!!errors.name}
          helperText={errors.name}
          disabled={isSubmitting}
          leadingIcon="Person"
        />

        <TextField
          label="Email Address"
          value={form.email}
          onChange={(value) => updateField('email', value)}
          placeholder="you@example.com"
          keyboardType="email"
          autoCapitalize="none"
          autoCorrect={false}
          error={!!errors.email}
          helperText={errors.email}
          disabled={isSubmitting}
          leadingIcon="Email"
        />

        <TextField
          label="Phone Number (Optional)"
          value={form.phone}
          onChange={(value) => updateField('phone', value)}
          placeholder="(555) 123-4567"
          keyboardType="phone"
          error={!!errors.phone}
          helperText={errors.phone || 'We may call if email is unsuccessful'}
          disabled={isSubmitting}
          leadingIcon="Phone"
        />

        <Picker
          label="Subject"
          value={form.subject}
          onChange={(value) => updateField('subject', value)}
          placeholder="Select a subject"
          options={subjectOptions}
          disabled={isSubmitting}
        />
        {errors.subject && (
          <Text style={styles.errorText}>{errors.subject}</Text>
        )}

        <Picker
          label="Preferred Contact Method"
          value={form.preferredContact}
          onChange={(value) => updateField('preferredContact', value)}
          options={contactMethodOptions}
          disabled={isSubmitting}
        />

        <DatePicker
          label="Preferred Callback Date (Optional)"
          value={form.preferredDate}
          onConfirm={(value) => updateField('preferredDate', value)}
          placeholder="Select a date"
          minDate={minDate}
          disabled={isSubmitting}
        />

        <TextField
          label="Message"
          value={form.message}
          onChange={(value) => updateField('message', value)}
          placeholder="How can we help you?"
          multiline
          maxLength={500}
          showCounter
          error={!!errors.message}
          helperText={errors.message}
          disabled={isSubmitting}
          style={styles.messageField}
        />

        <View style={styles.buttons}>
          <Button
            title="Clear"
            onPress={resetForm}
            disabled={isSubmitting}
            color="#888"
          />
          <Button
            title={isSubmitting ? 'Sending...' : 'Send Message'}
            onPress={handleSubmit}
            disabled={isSubmitting}
          />
        </View>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    textAlign: 'center',
    marginTop: 24,
    marginBottom: 8,
  },
  subtitle: {
    fontSize: 16,
    color: '#666',
    textAlign: 'center',
    marginBottom: 24,
    paddingHorizontal: 32,
  },
  form: {
    backgroundColor: '#fff',
    margin: 16,
    borderRadius: 12,
    padding: 16,
    gap: 16,
  },
  messageField: {
    minHeight: 120,
  },
  errorText: {
    color: '#B00020',
    fontSize: 12,
    marginTop: -12,
    marginLeft: 16,
  },
  buttons: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 8,
  },
});
```

## Key Features

- **Real-time validation** - Errors clear as user corrects input
- **Form-level validation** - All fields validated on submit
- **Loading state** - Form disabled during submission
- **Reset functionality** - Clear all fields
- **Accessible** - Proper labels and error messages
- **Material Icons** - Visual cues for input types

## Validation Patterns Used

1. **Required fields** - Name, email, subject, message
2. **Email format** - Regex validation
3. **Phone format** - Optional, but validated if provided
4. **Minimum length** - Message must be at least 10 characters
5. **Maximum length** - Message limited to 500 characters with counter
