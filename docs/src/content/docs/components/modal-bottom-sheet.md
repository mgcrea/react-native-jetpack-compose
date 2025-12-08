---
title: ModalBottomSheet
description: Draggable bottom sheet container for custom content
---

A modal bottom sheet component powered by Jetpack Compose's `ModalBottomSheet` for displaying custom content in a draggable sheet.

## Import

```tsx
import { ModalBottomSheet } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [visible, setVisible] = useState(false);

<Button title="Open Sheet" onPress={() => setVisible(true)} />

<ModalBottomSheet
  visible={visible}
  onDismiss={() => setVisible(false)}
>
  <View style={{ padding: 16 }}>
    <Text>Sheet Content</Text>
  </View>
</ModalBottomSheet>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `visible` | `boolean` | `false` | Controls sheet visibility |
| `showDragHandle` | `boolean` | `true` | Show drag handle at top |
| `maxHeightRatio` | `number` | `0.9` | Max height as ratio of screen (0-1) |
| `children` | `ReactNode` | - | Content to render inside sheet |
| `style` | `ViewStyle` | - | Container style |

## Events

| Event | Type | Description |
|-------|------|-------------|
| `onDismiss` | `() => void` | Called when sheet is dismissed |

## Examples

### Simple Content Sheet

```tsx
const [showInfo, setShowInfo] = useState(false);

<Button title="Show Info" onPress={() => setShowInfo(true)} />

<ModalBottomSheet
  visible={showInfo}
  onDismiss={() => setShowInfo(false)}
>
  <View style={styles.sheetContent}>
    <Text style={styles.title}>Information</Text>
    <Text>This is some information displayed in a bottom sheet.</Text>
    <Button title="Close" onPress={() => setShowInfo(false)} />
  </View>
</ModalBottomSheet>
```

### Action Sheet

```tsx
const [showActions, setShowActions] = useState(false);

<Button title="More Options" onPress={() => setShowActions(true)} />

<ModalBottomSheet
  visible={showActions}
  onDismiss={() => setShowActions(false)}
>
  <View style={styles.actions}>
    <TouchableOpacity
      style={styles.actionItem}
      onPress={() => {
        handleEdit();
        setShowActions(false);
      }}
    >
      <Text>Edit</Text>
    </TouchableOpacity>
    <TouchableOpacity
      style={styles.actionItem}
      onPress={() => {
        handleShare();
        setShowActions(false);
      }}
    >
      <Text>Share</Text>
    </TouchableOpacity>
    <TouchableOpacity
      style={[styles.actionItem, styles.destructive]}
      onPress={() => {
        handleDelete();
        setShowActions(false);
      }}
    >
      <Text style={{ color: 'red' }}>Delete</Text>
    </TouchableOpacity>
  </View>
</ModalBottomSheet>
```

### Form in Sheet

```tsx
const [showForm, setShowForm] = useState(false);
const [feedback, setFeedback] = useState('');

<Button title="Give Feedback" onPress={() => setShowForm(true)} />

<ModalBottomSheet
  visible={showForm}
  onDismiss={() => setShowForm(false)}
  maxHeightRatio={0.7}
>
  <View style={styles.form}>
    <Text style={styles.title}>Feedback</Text>
    <TextField
      label="Your Feedback"
      value={feedback}
      onChange={setFeedback}
      multiline
    />
    <View style={styles.buttons}>
      <Button title="Cancel" onPress={() => setShowForm(false)} />
      <Button
        title="Submit"
        onPress={() => {
          submitFeedback(feedback);
          setShowForm(false);
          setFeedback('');
        }}
      />
    </View>
  </View>
</ModalBottomSheet>
```

### Without Drag Handle

```tsx
<ModalBottomSheet
  visible={visible}
  onDismiss={() => setVisible(false)}
  showDragHandle={false}
>
  <View style={styles.content}>
    <Text>Content without drag handle</Text>
  </View>
</ModalBottomSheet>
```

### Controlled Height

```tsx
// Small sheet (30% of screen)
<ModalBottomSheet
  visible={visible}
  onDismiss={() => setVisible(false)}
  maxHeightRatio={0.3}
>
  <View style={styles.smallContent}>
    <Text>Compact sheet</Text>
  </View>
</ModalBottomSheet>

// Full-height sheet (90% of screen)
<ModalBottomSheet
  visible={visible}
  onDismiss={() => setVisible(false)}
  maxHeightRatio={0.9}
>
  <ScrollView style={styles.fullContent}>
    {/* Long content */}
  </ScrollView>
</ModalBottomSheet>
```

### Sheet with List

```tsx
const [showList, setShowList] = useState(false);
const items = ['Item 1', 'Item 2', 'Item 3', 'Item 4', 'Item 5'];

<ModalBottomSheet
  visible={showList}
  onDismiss={() => setShowList(false)}
>
  <ScrollView style={styles.list}>
    {items.map((item, index) => (
      <TouchableOpacity
        key={index}
        style={styles.listItem}
        onPress={() => {
          selectItem(item);
          setShowList(false);
        }}
      >
        <Text>{item}</Text>
      </TouchableOpacity>
    ))}
  </ScrollView>
</ModalBottomSheet>
```

### Confirmation Dialog

```tsx
const [showConfirm, setShowConfirm] = useState(false);

<ModalBottomSheet
  visible={showConfirm}
  onDismiss={() => setShowConfirm(false)}
  maxHeightRatio={0.3}
>
  <View style={styles.confirm}>
    <Text style={styles.title}>Delete Item?</Text>
    <Text>This action cannot be undone.</Text>
    <View style={styles.buttons}>
      <Button title="Cancel" onPress={() => setShowConfirm(false)} />
      <Button
        title="Delete"
        color="red"
        onPress={() => {
          performDelete();
          setShowConfirm(false);
        }}
      />
    </View>
  </View>
</ModalBottomSheet>
```

## Styling Example

```tsx
const styles = StyleSheet.create({
  sheetContent: {
    padding: 24,
    gap: 16,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
  },
  actions: {
    paddingVertical: 8,
  },
  actionItem: {
    paddingVertical: 16,
    paddingHorizontal: 24,
  },
  destructive: {
    borderTopWidth: 1,
    borderTopColor: '#eee',
  },
});
```

## Dismiss Behavior

The sheet can be dismissed by:
- Swiping down on the drag handle
- Tapping outside the sheet (on the scrim)
- Programmatically setting `visible={false}`

All dismiss actions trigger the `onDismiss` callback.

## Tips

- Always provide an `onDismiss` handler to update your `visible` state
- Use `maxHeightRatio` to control sheet size based on content
- Consider disabling the drag handle for sheets that shouldn't be swipe-dismissible
- Wrap long content in `ScrollView` for proper scrolling behavior
