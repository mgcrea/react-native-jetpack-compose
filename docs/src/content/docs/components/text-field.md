---
title: TextField
description: Material 3 text input component with labels, icons, validation, and keyboard options
---

A text field component powered by Jetpack Compose's `OutlinedTextField` with Material 3 styling.

## Import

```tsx
import { TextField } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [name, setName] = useState('');

<TextField
  label="Name"
  value={name}
  onChange={setName}
  placeholder="Enter your name"
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `value` | `string` | `''` | Current text value |
| `label` | `string` | - | Floating label text |
| `placeholder` | `string` | - | Placeholder text when empty |
| `disabled` | `boolean` | `false` | Disable the field |
| `editable` | `boolean` | `true` | Whether text can be edited |
| `multiline` | `boolean` | `false` | Enable multiline input |
| `maxLength` | `number` | - | Maximum character count |
| `secureTextEntry` | `boolean` | `false` | Hide text for passwords |
| `error` | `boolean` | `false` | Show error state styling |
| `helperText` | `string` | - | Helper or error text below field |
| `keyboardType` | `KeyboardType` | `'default'` | Keyboard type to show |
| `returnKeyType` | `ReturnKeyType` | `'done'` | IME action button type |
| `autoCapitalize` | `AutoCapitalize` | `'sentences'` | Auto-capitalization behavior |
| `autoCorrect` | `boolean` | `true` | Enable auto-correct |
| `leadingIcon` | `string` | - | Leading Material Icon name |
| `trailingIcon` | `string` | - | Trailing Material Icon name |
| `showCounter` | `boolean` | `true` | Show character counter with maxLength |
| `style` | `ViewStyle` | - | Container style |

## Events

| Event | Type | Description |
|-------|------|-------------|
| `onChange` | `(text: string) => void` | Called when text changes |
| `onFocus` | `() => void` | Called when field gains focus |
| `onBlur` | `() => void` | Called when field loses focus |
| `onSubmitEditing` | `() => void` | Called when IME action pressed |
| `onTrailingIconPress` | `() => void` | Called when trailing icon tapped |

## Keyboard Types

```tsx
<TextField keyboardType="default" />   // Standard keyboard
<TextField keyboardType="email" />     // Email keyboard with @
<TextField keyboardType="number" />    // Numeric keyboard
<TextField keyboardType="phone" />     // Phone dial pad
<TextField keyboardType="decimal" />   // Numbers with decimal
<TextField keyboardType="url" />       // URL keyboard with /
```

## Return Key Types

```tsx
<TextField returnKeyType="done" />    // Done button
<TextField returnKeyType="go" />      // Go button
<TextField returnKeyType="next" />    // Next button
<TextField returnKeyType="search" />  // Search button
<TextField returnKeyType="send" />    // Send button
```

## Examples

### Email Input

```tsx
const [email, setEmail] = useState('');

<TextField
  label="Email"
  value={email}
  onChange={setEmail}
  placeholder="you@example.com"
  keyboardType="email"
  autoCapitalize="none"
  autoCorrect={false}
  leadingIcon="Email"
/>
```

### Password Input

```tsx
const [password, setPassword] = useState('');
const [showPassword, setShowPassword] = useState(false);

<TextField
  label="Password"
  value={password}
  onChange={setPassword}
  secureTextEntry={!showPassword}
  trailingIcon={showPassword ? "VisibilityOff" : "Visibility"}
  onTrailingIconPress={() => setShowPassword(!showPassword)}
/>
```

### Search Field

```tsx
const [search, setSearch] = useState('');

<TextField
  label="Search"
  value={search}
  onChange={setSearch}
  placeholder="Search..."
  leadingIcon="Search"
  trailingIcon={search ? "Clear" : undefined}
  onTrailingIconPress={() => setSearch('')}
  returnKeyType="search"
  onSubmitEditing={() => performSearch(search)}
/>
```

### Validation with Error State

```tsx
const [email, setEmail] = useState('');
const [error, setError] = useState(false);

const validateEmail = (text: string) => {
  setEmail(text);
  setError(text.length > 0 && !text.includes('@'));
};

<TextField
  label="Email"
  value={email}
  onChange={validateEmail}
  error={error}
  helperText={error ? 'Please enter a valid email address' : 'We will never share your email'}
  keyboardType="email"
/>
```

### Character Limit

```tsx
const [bio, setBio] = useState('');

<TextField
  label="Bio"
  value={bio}
  onChange={setBio}
  placeholder="Tell us about yourself"
  multiline
  maxLength={200}
  showCounter
/>
```

### Multiline Text Area

```tsx
const [message, setMessage] = useState('');

<TextField
  label="Message"
  value={message}
  onChange={setMessage}
  placeholder="Enter your message..."
  multiline
  style={{ minHeight: 120 }}
/>
```

### Form with Focus Management

```tsx
const [firstName, setFirstName] = useState('');
const [lastName, setLastName] = useState('');

<TextField
  label="First Name"
  value={firstName}
  onChange={setFirstName}
  returnKeyType="next"
  onSubmitEditing={() => {
    // Focus next field (implement with refs)
  }}
/>
<TextField
  label="Last Name"
  value={lastName}
  onChange={setLastName}
  returnKeyType="done"
  onSubmitEditing={() => {
    // Submit form
  }}
/>
```

## Material Icons

The `leadingIcon` and `trailingIcon` props accept Material Icon names. Common icons include:

- `Search`, `Clear`, `Close`
- `Email`, `Phone`, `Person`
- `Visibility`, `VisibilityOff`
- `Lock`, `Key`
- `Edit`, `Delete`
- `Check`, `Error`, `Warning`

See the [Material Icons](https://fonts.google.com/icons) catalog for the full list.
