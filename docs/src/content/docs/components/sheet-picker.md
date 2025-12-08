---
title: SheetPicker
description: Searchable picker with a modal bottom sheet for long option lists
---

A searchable picker component that opens a modal bottom sheet with a search field. Ideal for long lists like countries, languages, or categories.

## Import

```tsx
import { SheetPicker } from '@mgcrea/react-native-jetpack-compose';
```

## Basic Usage

```tsx
const [country, setCountry] = useState<string | null>(null);

<SheetPicker
  label="Country"
  value={country}
  onSelect={setCountry}
  title="Select Country"
  options={[
    { value: 'us', label: 'United States' },
    { value: 'uk', label: 'United Kingdom' },
    { value: 'ca', label: 'Canada' },
    // ... more options
  ]}
/>
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `options` | `SheetPickerOption[]` | **Required** | Array of selectable options |
| `value` | `string \| null` | `null` | Currently selected value |
| `title` | `string` | - | Title at top of sheet |
| `searchPlaceholder` | `string` | - | Placeholder for search field |
| `autoDismiss` | `boolean` | `true` | Auto-dismiss after selection |
| `maxHeightRatio` | `number` | `0.9` | Max height as ratio of screen (0-1) |
| `sheetMaxWidth` | `number` | - | Max width in dp (centers sheet) |
| `label` | `string` | - | Floating label for text field |
| `placeholder` | `string` | - | Placeholder when no selection |
| `disabled` | `boolean` | `false` | Disable the picker |
| `style` | `ViewStyle` | - | Container style |

## Events

| Event | Type | Description |
|-------|------|-------------|
| `onSelect` | `(value: string) => void` | Called when option selected |
| `onDismiss` | `() => void` | Called when sheet dismissed |

## Types

```tsx
interface SheetPickerOption {
  value: string;  // Unique identifier
  label: string;  // Display text (also used for search)
}
```

## Examples

### Country Selector

```tsx
const [country, setCountry] = useState<string | null>(null);

const countries = [
  { value: 'af', label: 'Afghanistan' },
  { value: 'al', label: 'Albania' },
  { value: 'dz', label: 'Algeria' },
  // ... 200+ countries
  { value: 'zw', label: 'Zimbabwe' },
];

<SheetPicker
  label="Country"
  value={country}
  onSelect={setCountry}
  title="Select Country"
  searchPlaceholder="Search countries..."
  options={countries}
/>
```

### Language Selector

```tsx
const [language, setLanguage] = useState<string | null>('en');

<SheetPicker
  label="Preferred Language"
  value={language}
  onSelect={setLanguage}
  title="Select Language"
  searchPlaceholder="Search languages..."
  options={[
    { value: 'en', label: 'English' },
    { value: 'es', label: 'Spanish' },
    { value: 'fr', label: 'French' },
    { value: 'de', label: 'German' },
    { value: 'it', label: 'Italian' },
    { value: 'pt', label: 'Portuguese' },
    { value: 'zh', label: 'Chinese' },
    { value: 'ja', label: 'Japanese' },
    { value: 'ko', label: 'Korean' },
    { value: 'ar', label: 'Arabic' },
  ]}
/>
```

### Without Auto-Dismiss

Keep the sheet open after selection (useful for multi-step flows):

```tsx
const [value, setValue] = useState<string | null>(null);

<SheetPicker
  label="Selection"
  value={value}
  onSelect={setValue}
  autoDismiss={false}
  onDismiss={() => console.log('Sheet closed')}
  options={options}
/>
```

### Custom Sheet Size

Control the sheet height and width:

```tsx
<SheetPicker
  label="Selection"
  value={value}
  onSelect={setValue}
  maxHeightRatio={0.5}    // Half screen height
  sheetMaxWidth={400}     // Max 400dp wide, centered
  options={options}
/>
```

### Category Selection with Groups

For categorized options, format labels to indicate grouping:

```tsx
const categories = [
  { value: 'electronics-phones', label: 'Electronics > Phones' },
  { value: 'electronics-laptops', label: 'Electronics > Laptops' },
  { value: 'electronics-tablets', label: 'Electronics > Tablets' },
  { value: 'clothing-mens', label: 'Clothing > Men\'s' },
  { value: 'clothing-womens', label: 'Clothing > Women\'s' },
  { value: 'home-furniture', label: 'Home > Furniture' },
  { value: 'home-decor', label: 'Home > Decor' },
];

<SheetPicker
  label="Category"
  value={category}
  onSelect={setCategory}
  title="Select Category"
  searchPlaceholder="Search categories..."
  options={categories}
/>
```

### Dynamic Options from API

```tsx
const [products, setProducts] = useState<SheetPickerOption[]>([]);
const [selectedProduct, setSelectedProduct] = useState<string | null>(null);

useEffect(() => {
  fetchProducts().then(data => {
    setProducts(data.map(p => ({
      value: p.id,
      label: p.name,
    })));
  });
}, []);

<SheetPicker
  label="Product"
  value={selectedProduct}
  onSelect={setSelectedProduct}
  title="Select Product"
  searchPlaceholder="Search products..."
  options={products}
/>
```

## Comparison with Picker

| Feature | Picker | SheetPicker |
|---------|--------|-------------|
| Best for | Short lists (<10 items) | Long lists (10+ items) |
| Search | No | Yes |
| Presentation | Dropdown menu | Bottom sheet |
| Screen usage | Minimal | Up to 90% height |

## Tips

- Use descriptive labels in options - they're used for search matching
- Set a meaningful `title` to help users understand what they're selecting
- For very long lists, ensure options are sorted logically (alphabetically, by popularity, etc.)
