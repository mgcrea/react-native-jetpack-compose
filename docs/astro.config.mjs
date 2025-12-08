// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

// https://astro.build/config
export default defineConfig({
	site: 'https://mgcrea.github.io',
	base: '/react-native-jetpack-compose',
	integrations: [
		starlight({
			title: 'React Native Jetpack Compose',
			social: [
				{ icon: 'github', label: 'GitHub', href: 'https://github.com/mgcrea/react-native-jetpack-compose' },
			],
			sidebar: [
				{
					label: 'Getting Started',
					items: [
						{ label: 'Introduction', slug: 'getting-started/introduction' },
						{ label: 'Installation', slug: 'getting-started/installation' },
						{ label: 'Quick Start', slug: 'getting-started/quick-start' },
						{ label: 'How It Works', slug: 'getting-started/how-it-works' },
					],
				},
				{
					label: 'Components',
					items: [
						{ label: 'Overview', slug: 'components' },
						{ label: 'TextField', slug: 'components/text-field' },
						{ label: 'Picker', slug: 'components/picker' },
						{ label: 'SheetPicker', slug: 'components/sheet-picker' },
						{ label: 'DatePicker', slug: 'components/date-picker' },
						{ label: 'DateRangePicker', slug: 'components/date-range-picker' },
						{ label: 'TimePicker', slug: 'components/time-picker' },
						{ label: 'TimeRangePicker', slug: 'components/time-range-picker' },
						{ label: 'ModalBottomSheet', slug: 'components/modal-bottom-sheet' },
					],
				},
				{
					label: 'Guides',
					items: [
						{ label: 'Building Forms', slug: 'guides/building-forms' },
						{ label: 'Date & Time Handling', slug: 'guides/date-time-handling' },
						{ label: 'Styling', slug: 'guides/styling' },
						{ label: 'Event Handling', slug: 'guides/event-handling' },
					],
				},
				{
					label: 'Examples',
					autogenerate: { directory: 'examples' },
				},
			],
		}),
	],
});
