---
title: Settings Screen
description: A settings screen demonstrating various picker components
---

A user settings screen showcasing SheetPicker, Picker, TimePicker, and ModalBottomSheet for preferences management.

## Complete Example

```tsx
import { useState, useCallback } from 'react';
import { View, Text, Button, Switch, Alert, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import {
  TextField,
  Picker,
  SheetPicker,
  TimePicker,
  TimeRangePicker,
  ModalBottomSheet,
  type TimeRange,
} from '@mgcrea/react-native-jetpack-compose';

// Sample data
const languages = [
  { value: 'en', label: 'English' },
  { value: 'es', label: 'Español' },
  { value: 'fr', label: 'Français' },
  { value: 'de', label: 'Deutsch' },
  { value: 'it', label: 'Italiano' },
  { value: 'pt', label: 'Português' },
  { value: 'zh', label: '中文' },
  { value: 'ja', label: '日本語' },
  { value: 'ko', label: '한국어' },
  { value: 'ar', label: 'العربية' },
];

const timezones = [
  { value: 'America/New_York', label: 'Eastern Time (ET)' },
  { value: 'America/Chicago', label: 'Central Time (CT)' },
  { value: 'America/Denver', label: 'Mountain Time (MT)' },
  { value: 'America/Los_Angeles', label: 'Pacific Time (PT)' },
  { value: 'America/Anchorage', label: 'Alaska Time (AKT)' },
  { value: 'Pacific/Honolulu', label: 'Hawaii Time (HT)' },
  { value: 'Europe/London', label: 'Greenwich Mean Time (GMT)' },
  { value: 'Europe/Paris', label: 'Central European Time (CET)' },
  { value: 'Asia/Tokyo', label: 'Japan Standard Time (JST)' },
  { value: 'Asia/Shanghai', label: 'China Standard Time (CST)' },
  { value: 'Australia/Sydney', label: 'Australian Eastern Time (AET)' },
];

const themes = [
  { value: 'system', label: 'System Default' },
  { value: 'light', label: 'Light' },
  { value: 'dark', label: 'Dark' },
];

const notificationFrequencies = [
  { value: 'realtime', label: 'Real-time' },
  { value: 'hourly', label: 'Hourly Digest' },
  { value: 'daily', label: 'Daily Digest' },
  { value: 'weekly', label: 'Weekly Digest' },
  { value: 'none', label: 'None' },
];

interface Settings {
  // Profile
  displayName: string;
  email: string;
  // Preferences
  language: string;
  timezone: string;
  theme: string;
  // Notifications
  notificationsEnabled: boolean;
  notificationFrequency: string | null;
  quietHoursEnabled: boolean;
  quietHours: TimeRange | null;
  dailyReminderEnabled: boolean;
  dailyReminderTime: Date | null;
}

export function SettingsScreen() {
  const [settings, setSettings] = useState<Settings>({
    displayName: 'John Doe',
    email: 'john.doe@example.com',
    language: 'en',
    timezone: 'America/New_York',
    theme: 'system',
    notificationsEnabled: true,
    notificationFrequency: 'realtime',
    quietHoursEnabled: false,
    quietHours: null,
    dailyReminderEnabled: true,
    dailyReminderTime: (() => {
      const d = new Date();
      d.setHours(9, 0, 0, 0);
      return d;
    })(),
  });

  const [showLogoutSheet, setShowLogoutSheet] = useState(false);
  const [hasChanges, setHasChanges] = useState(false);

  const updateSetting = useCallback(<K extends keyof Settings>(
    key: K,
    value: Settings[K]
  ) => {
    setSettings(prev => ({ ...prev, [key]: value }));
    setHasChanges(true);
  }, []);

  const handleSave = () => {
    // Simulate saving
    Alert.alert('Success', 'Settings saved successfully');
    setHasChanges(false);
  };

  const handleLogout = () => {
    setShowLogoutSheet(false);
    Alert.alert('Logged Out', 'You have been logged out');
  };

  const formatTime = (time: Date | null): string => {
    if (!time) return 'Not set';
    return time.toLocaleTimeString('en-US', {
      hour: 'numeric',
      minute: '2-digit',
      hour12: true,
    });
  };

  const formatTimeRange = (range: TimeRange | null): string => {
    if (!range?.startTime || !range?.endTime) return 'Not set';
    return `${formatTime(range.startTime)} - ${formatTime(range.endTime)}`;
  };

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Settings</Text>
        {hasChanges && (
          <Button title="Save" onPress={handleSave} />
        )}
      </View>

      {/* Profile Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Profile</Text>

        <TextField
          label="Display Name"
          value={settings.displayName}
          onChange={(v) => updateSetting('displayName', v)}
        />

        <TextField
          label="Email"
          value={settings.email}
          onChange={(v) => updateSetting('email', v)}
          keyboardType="email"
          autoCapitalize="none"
        />
      </View>

      {/* Preferences Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Preferences</Text>

        <SheetPicker
          label="Language"
          value={settings.language}
          onSelect={(v) => updateSetting('language', v)}
          title="Select Language"
          searchPlaceholder="Search languages..."
          options={languages}
        />

        <SheetPicker
          label="Timezone"
          value={settings.timezone}
          onSelect={(v) => updateSetting('timezone', v)}
          title="Select Timezone"
          searchPlaceholder="Search timezones..."
          options={timezones}
        />

        <Picker
          label="Theme"
          value={settings.theme}
          onChange={(v) => updateSetting('theme', v)}
          options={themes}
        />
      </View>

      {/* Notifications Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Notifications</Text>

        <View style={styles.switchRow}>
          <Text style={styles.switchLabel}>Enable Notifications</Text>
          <Switch
            value={settings.notificationsEnabled}
            onValueChange={(v) => updateSetting('notificationsEnabled', v)}
          />
        </View>

        {settings.notificationsEnabled && (
          <>
            <Picker
              label="Notification Frequency"
              value={settings.notificationFrequency}
              onChange={(v) => updateSetting('notificationFrequency', v)}
              options={notificationFrequencies}
            />

            <View style={styles.switchRow}>
              <View>
                <Text style={styles.switchLabel}>Quiet Hours</Text>
                <Text style={styles.switchDescription}>
                  {settings.quietHoursEnabled
                    ? formatTimeRange(settings.quietHours)
                    : 'Disabled'}
                </Text>
              </View>
              <Switch
                value={settings.quietHoursEnabled}
                onValueChange={(v) => updateSetting('quietHoursEnabled', v)}
              />
            </View>

            {settings.quietHoursEnabled && (
              <TimeRangePicker
                label="Quiet Hours"
                value={settings.quietHours}
                onConfirm={(v) => updateSetting('quietHours', v)}
                title="Set Quiet Hours"
              />
            )}

            <View style={styles.switchRow}>
              <View>
                <Text style={styles.switchLabel}>Daily Reminder</Text>
                <Text style={styles.switchDescription}>
                  {settings.dailyReminderEnabled
                    ? formatTime(settings.dailyReminderTime)
                    : 'Disabled'}
                </Text>
              </View>
              <Switch
                value={settings.dailyReminderEnabled}
                onValueChange={(v) => updateSetting('dailyReminderEnabled', v)}
              />
            </View>

            {settings.dailyReminderEnabled && (
              <TimePicker
                label="Reminder Time"
                value={settings.dailyReminderTime}
                onConfirm={(v) => updateSetting('dailyReminderTime', v)}
                title="Set Reminder Time"
              />
            )}
          </>
        )}
      </View>

      {/* Account Section */}
      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Account</Text>

        <TouchableOpacity
          style={styles.dangerButton}
          onPress={() => setShowLogoutSheet(true)}
        >
          <Text style={styles.dangerButtonText}>Log Out</Text>
        </TouchableOpacity>
      </View>

      {/* Logout Confirmation Sheet */}
      <ModalBottomSheet
        visible={showLogoutSheet}
        onDismiss={() => setShowLogoutSheet(false)}
        maxHeightRatio={0.3}
      >
        <View style={styles.logoutSheet}>
          <Text style={styles.logoutTitle}>Log Out?</Text>
          <Text style={styles.logoutMessage}>
            Are you sure you want to log out? You'll need to sign in again to access your account.
          </Text>
          <View style={styles.logoutButtons}>
            <TouchableOpacity
              style={styles.cancelButton}
              onPress={() => setShowLogoutSheet(false)}
            >
              <Text style={styles.cancelButtonText}>Cancel</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.logoutButton}
              onPress={handleLogout}
            >
              <Text style={styles.logoutButtonText}>Log Out</Text>
            </TouchableOpacity>
          </View>
        </View>
      </ModalBottomSheet>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 16,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
  },
  section: {
    backgroundColor: '#fff',
    marginHorizontal: 16,
    marginBottom: 16,
    borderRadius: 12,
    padding: 16,
    gap: 12,
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 8,
  },
  switchRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 8,
  },
  switchLabel: {
    fontSize: 16,
  },
  switchDescription: {
    fontSize: 14,
    color: '#666',
    marginTop: 2,
  },
  dangerButton: {
    backgroundColor: '#ffebee',
    padding: 16,
    borderRadius: 8,
    alignItems: 'center',
  },
  dangerButtonText: {
    color: '#c62828',
    fontWeight: '600',
    fontSize: 16,
  },
  logoutSheet: {
    padding: 24,
  },
  logoutTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 8,
  },
  logoutMessage: {
    fontSize: 16,
    color: '#666',
    marginBottom: 24,
  },
  logoutButtons: {
    flexDirection: 'row',
    gap: 12,
  },
  cancelButton: {
    flex: 1,
    padding: 16,
    borderRadius: 8,
    backgroundColor: '#f0f0f0',
    alignItems: 'center',
  },
  cancelButtonText: {
    fontSize: 16,
    fontWeight: '600',
  },
  logoutButton: {
    flex: 1,
    padding: 16,
    borderRadius: 8,
    backgroundColor: '#c62828',
    alignItems: 'center',
  },
  logoutButtonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
});
```

## Key Features

- **Sectioned layout** - Organized into Profile, Preferences, Notifications, Account
- **SheetPicker for long lists** - Language and timezone selection with search
- **Conditional fields** - Notification settings shown only when enabled
- **Time settings** - Daily reminder time and quiet hours range
- **ModalBottomSheet** - Confirmation dialog for logout action
- **Unsaved changes indicator** - Save button appears when settings modified

## Components Demonstrated

1. **TextField** - Profile information editing
2. **SheetPicker** - Searchable language and timezone selection
3. **Picker** - Theme and notification frequency dropdowns
4. **TimePicker** - Daily reminder time setting
5. **TimeRangePicker** - Quiet hours configuration
6. **ModalBottomSheet** - Logout confirmation dialog
7. **Switch** - Toggle settings (React Native built-in)

## Patterns Used

- **Conditional rendering** - Show/hide fields based on toggle state
- **Formatted display** - Time values shown in human-readable format
- **Change tracking** - Detect when settings have been modified
- **Confirmation dialogs** - ModalBottomSheet for destructive actions
