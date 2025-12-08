---
title: Booking Form
description: A reservation form demonstrating date and time picker integration
---

A reservation booking form showcasing DatePicker, DateRangePicker, TimePicker, and dependent field validation.

## Complete Example

```tsx
import { useState, useCallback, useMemo } from 'react';
import { View, Button, Text, Alert, StyleSheet, ScrollView } from 'react-native';
import {
  TextField,
  Picker,
  SheetPicker,
  DatePicker,
  DateRangePicker,
  TimePicker,
  type DateRange,
} from '@mgcrea/react-native-jetpack-compose';

interface BookingData {
  guestName: string;
  email: string;
  phone: string;
  bookingType: string | null;
  // Single day booking
  singleDate: Date | null;
  checkInTime: Date | null;
  checkOutTime: Date | null;
  // Multi-day booking
  dateRange: DateRange | null;
  roomType: string | null;
  guests: string | null;
  specialRequests: string;
}

const bookingTypes = [
  { value: 'day', label: 'Day Use (Same Day)' },
  { value: 'stay', label: 'Overnight Stay' },
];

const roomTypes = [
  { value: 'standard', label: 'Standard Room - $99/night' },
  { value: 'deluxe', label: 'Deluxe Room - $149/night' },
  { value: 'suite', label: 'Executive Suite - $249/night' },
  { value: 'penthouse', label: 'Penthouse - $499/night' },
];

const guestOptions = [
  { value: '1', label: '1 Guest' },
  { value: '2', label: '2 Guests' },
  { value: '3', label: '3 Guests' },
  { value: '4', label: '4 Guests' },
];

export function BookingForm() {
  const [booking, setBooking] = useState<BookingData>({
    guestName: '',
    email: '',
    phone: '',
    bookingType: null,
    singleDate: null,
    checkInTime: null,
    checkOutTime: null,
    dateRange: null,
    roomType: null,
    guests: '2',
    specialRequests: '',
  });

  const [isSubmitting, setIsSubmitting] = useState(false);

  const updateField = useCallback(<K extends keyof BookingData>(
    field: K,
    value: BookingData[K]
  ) => {
    setBooking(prev => ({ ...prev, [field]: value }));
  }, []);

  // Calculate dates
  const today = useMemo(() => new Date(), []);
  const maxDate = useMemo(() => {
    const date = new Date();
    date.setMonth(date.getMonth() + 6);
    return date;
  }, []);

  // Calculate pricing
  const calculateTotal = useMemo(() => {
    if (!booking.roomType) return null;

    const prices: Record<string, number> = {
      standard: 99,
      deluxe: 149,
      suite: 249,
      penthouse: 499,
    };

    const pricePerNight = prices[booking.roomType] || 0;

    if (booking.bookingType === 'day') {
      return pricePerNight * 0.5; // Day use is half price
    }

    if (booking.dateRange?.startDate && booking.dateRange?.endDate) {
      const nights = Math.ceil(
        (booking.dateRange.endDate.getTime() - booking.dateRange.startDate.getTime()) /
        (1000 * 60 * 60 * 24)
      );
      return pricePerNight * nights;
    }

    return null;
  }, [booking.roomType, booking.bookingType, booking.dateRange]);

  // Calculate duration for display
  const stayDuration = useMemo(() => {
    if (!booking.dateRange?.startDate || !booking.dateRange?.endDate) return null;

    const nights = Math.ceil(
      (booking.dateRange.endDate.getTime() - booking.dateRange.startDate.getTime()) /
      (1000 * 60 * 60 * 24)
    );

    return nights === 1 ? '1 night' : `${nights} nights`;
  }, [booking.dateRange]);

  const handleSubmit = async () => {
    // Basic validation
    if (!booking.guestName || !booking.email || !booking.bookingType || !booking.roomType) {
      Alert.alert('Error', 'Please fill in all required fields');
      return;
    }

    if (booking.bookingType === 'day' && !booking.singleDate) {
      Alert.alert('Error', 'Please select a date');
      return;
    }

    if (booking.bookingType === 'stay' &&
        (!booking.dateRange?.startDate || !booking.dateRange?.endDate)) {
      Alert.alert('Error', 'Please select check-in and check-out dates');
      return;
    }

    setIsSubmitting(true);

    try {
      await new Promise(resolve => setTimeout(resolve, 1500));

      Alert.alert(
        'Booking Confirmed!',
        `Thank you, ${booking.guestName}!\n\nTotal: $${calculateTotal?.toFixed(2)}\n\nA confirmation email will be sent to ${booking.email}`,
        [{ text: 'OK' }]
      );
    } catch (error) {
      Alert.alert('Error', 'Failed to process booking. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const isDayBooking = booking.bookingType === 'day';
  const isStayBooking = booking.bookingType === 'stay';

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Book Your Stay</Text>

      <View style={styles.form}>
        <Text style={styles.sectionTitle}>Guest Information</Text>

        <TextField
          label="Full Name"
          value={booking.guestName}
          onChange={(v) => updateField('guestName', v)}
          placeholder="Enter your name"
          disabled={isSubmitting}
        />

        <TextField
          label="Email"
          value={booking.email}
          onChange={(v) => updateField('email', v)}
          placeholder="you@example.com"
          keyboardType="email"
          autoCapitalize="none"
          disabled={isSubmitting}
        />

        <TextField
          label="Phone"
          value={booking.phone}
          onChange={(v) => updateField('phone', v)}
          placeholder="(555) 123-4567"
          keyboardType="phone"
          disabled={isSubmitting}
        />

        <Text style={styles.sectionTitle}>Reservation Details</Text>

        <Picker
          label="Booking Type"
          value={booking.bookingType}
          onChange={(v) => {
            updateField('bookingType', v);
            // Reset date selections when type changes
            updateField('singleDate', null);
            updateField('dateRange', null);
            updateField('checkInTime', null);
            updateField('checkOutTime', null);
          }}
          placeholder="Select booking type"
          options={bookingTypes}
          disabled={isSubmitting}
        />

        {isDayBooking && (
          <>
            <DatePicker
              label="Date"
              value={booking.singleDate}
              onConfirm={(v) => updateField('singleDate', v)}
              minDate={today}
              maxDate={maxDate}
              disabled={isSubmitting}
            />

            <View style={styles.row}>
              <View style={styles.flex1}>
                <TimePicker
                  label="Check-in Time"
                  value={booking.checkInTime}
                  onConfirm={(v) => updateField('checkInTime', v)}
                  disabled={isSubmitting || !booking.singleDate}
                />
              </View>
              <View style={styles.flex1}>
                <TimePicker
                  label="Check-out Time"
                  value={booking.checkOutTime}
                  onConfirm={(v) => updateField('checkOutTime', v)}
                  disabled={isSubmitting || !booking.checkInTime}
                />
              </View>
            </View>
          </>
        )}

        {isStayBooking && (
          <>
            <DateRangePicker
              label="Stay Dates"
              value={booking.dateRange}
              onConfirm={(v) => updateField('dateRange', v)}
              minDate={today}
              maxDate={maxDate}
              title="Select check-in and check-out dates"
              disabled={isSubmitting}
            />

            {stayDuration && (
              <Text style={styles.durationText}>
                Duration: {stayDuration}
              </Text>
            )}
          </>
        )}

        {(isDayBooking || isStayBooking) && (
          <>
            <SheetPicker
              label="Room Type"
              value={booking.roomType}
              onSelect={(v) => updateField('roomType', v)}
              title="Select Room Type"
              options={roomTypes}
              disabled={isSubmitting}
            />

            <Picker
              label="Number of Guests"
              value={booking.guests}
              onChange={(v) => updateField('guests', v)}
              options={guestOptions}
              disabled={isSubmitting}
            />

            <TextField
              label="Special Requests (Optional)"
              value={booking.specialRequests}
              onChange={(v) => updateField('specialRequests', v)}
              placeholder="Any special requirements?"
              multiline
              maxLength={200}
              disabled={isSubmitting}
            />
          </>
        )}

        {calculateTotal !== null && (
          <View style={styles.totalSection}>
            <Text style={styles.totalLabel}>Estimated Total:</Text>
            <Text style={styles.totalAmount}>${calculateTotal.toFixed(2)}</Text>
          </View>
        )}

        <Button
          title={isSubmitting ? 'Processing...' : 'Complete Booking'}
          onPress={handleSubmit}
          disabled={isSubmitting || !booking.bookingType}
        />
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
    marginBottom: 16,
  },
  form: {
    backgroundColor: '#fff',
    margin: 16,
    borderRadius: 12,
    padding: 16,
    gap: 16,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginTop: 8,
    marginBottom: 4,
  },
  row: {
    flexDirection: 'row',
    gap: 16,
  },
  flex1: {
    flex: 1,
  },
  durationText: {
    fontSize: 14,
    color: '#666',
    marginTop: -8,
    marginLeft: 4,
  },
  totalSection: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 16,
    borderTopWidth: 1,
    borderTopColor: '#eee',
    marginTop: 8,
  },
  totalLabel: {
    fontSize: 18,
    fontWeight: '600',
  },
  totalAmount: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#2196F3',
  },
});
```

## Key Features

- **Conditional fields** - Shows different date inputs based on booking type
- **Dependent fields** - Check-out time disabled until check-in selected
- **Dynamic pricing** - Calculates total based on room type and duration
- **Date constraints** - Only allows booking up to 6 months in advance
- **Duration display** - Shows calculated number of nights

## Patterns Demonstrated

1. **Conditional rendering** based on booking type
2. **DateRangePicker** for multi-day stays
3. **TimePicker** for day-use check-in/out times
4. **SheetPicker** for room selection with prices
5. **Real-time price calculation** using useMemo
6. **Field dependencies** - later fields depend on earlier selections
