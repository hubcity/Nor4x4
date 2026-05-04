## 2024-05-14 - Prevent TalkBack from redundantly announcing visual text
**Learning:** When using Jetpack Compose, applying a custom `contentDescription` to a `Text` composable using standard `Modifier.semantics` causes screen readers like TalkBack to read both the visible text and the content description. This results in redundant readouts like "05:00, 5 minutes and 0 seconds remaining".
**Action:** Always use `Modifier.clearAndSetSemantics` instead of `Modifier.semantics` when applying a custom content description to a `Text` element. This clears the literal text from the semantic tree, ensuring only the intended natural language string is announced.
## 2024-04-25 - Jetpack Compose Box Accessibility
**Learning:** Custom clickable components created with `Box` and `Modifier.clickable` in Jetpack Compose for Wear OS do not automatically announce their role as buttons to screen readers, unlike the standard `Button` component.
**Action:** Always add `role = Role.Button` to `Modifier.clickable()` when building custom button-like elements using generic containers like `Box` or `Row`.

## 2024-04-25 - Jetpack Compose Semantic Merging and Natural Language Readouts
**Learning:** Timer formatting (like "04:00") is read poorly by TalkBack ("four colon zero zero"). Also, composite readouts (like a Row with "125" text and a Heart Icon) are read disjointedly. Wear OS users, especially during workouts, rely heavily on accurate, consolidated audio feedback.
**Action:** Use `Modifier.semantics { contentDescription = "..." }` to replace plain text with natural language phrases (e.g., "four minutes and zero seconds remaining"). Use `Modifier.semantics(mergeDescendants = true) { ... }` on parent layout components (like Row) to combine separate visual elements into a single coherent screen reader announcement.
## 2026-04-22 - Jetpack Compose Timer Formats and Progress Accessibility
**Learning:** Timer formatting (like "04:00") is read poorly by TalkBack ("four colon zero zero"). Furthermore, `CircularProgressIndicator` instances without semantic descriptions offer zero feedback to non-visual users during loading states.
**Action:** Use `Modifier.semantics { contentDescription = ... }` to map text values like "04:00" to natural language properties ("4 minutes and 0 seconds") and add loading descriptions to ambiguous indicators.
## 2024-04-25 - Jetpack Compose Modifier.clearAndSetSemantics
**Learning:** Using `Modifier.semantics` on Text elements that already have visual formatting causes screen readers to announce both the visual text and the content description sequentially (e.g., "04:00, four minutes and zero seconds remaining"). This redundant feedback is confusing and verbose, especially during a workout.
**Action:** Always use `Modifier.clearAndSetSemantics` instead of standard `Modifier.semantics` on Text elements when you want to completely replace the default textual announcement with a custom content description.
## 2024-05-18 - Visual feedback for bounded limits
**Learning:** In custom configuration screens, numeric settings often have a minimum allowed limit (like minimum 1 rep or minimum 30 seconds interval). Leaving the decrement button fully interactive when this limit is reached results in fruitless clicks and user confusion.
**Action:** Always provide visual feedback by dynamically setting the `enabled` state of decrease/increase buttons when the upper or lower bounds are reached.
## 2024-05-19 - Screen Reader Heading Navigation
**Learning:** In Wear OS applications, relying solely on sequential reading can be tedious for screen reader users on small screens. Marking key structural elements (like screen titles) as headings allows TalkBack users to quickly jump between sections.
**Action:** Always add `Modifier.semantics { heading() }` to screen titles or major section dividers to improve navigational accessibility.

## 2024-05-02 - [Wear OS Chip Icons & Progress Indicator Semantics]
**Learning:** Using `Modifier.clearAndSetSemantics` is crucial for ambiguous visual indicators like `CircularProgressIndicator` on Wear OS to avoid TalkBack reading out literal visual formats instead of the intended loading text. Also, Wear OS `Chip` icons should be sized with `ChipDefaults.IconSize` and have `contentDescription = null` if text labels are already present to avoid redundant announcements.
**Action:** Always verify `CircularProgressIndicator` semantics and appropriately size/silence decorative `Chip` icons in Wear OS UI components.
## 2026-05-03 - Natural Language Timer Accessibility\n**Learning:** TalkBack awkwardly reads "0 seconds" or pluralizes "1 minutes" if singulars and zeroes aren't explicitly handled in  logic. Reading "1 minute and 0 seconds" is verbose for a workout timer.\n**Action:** When mapping time to string formats for , always handle singular/plural strings (e.g. '1 minute') and omit the zero segments entirely for brevity and natural flow.
## 2026-05-03 - Natural Language Timer Accessibility
**Learning:** TalkBack awkwardly reads '0 seconds' or pluralizes '1 minutes' if singulars and zeroes aren't explicitly handled in content description logic. Reading '1 minute and 0 seconds' is verbose for a workout timer.
**Action:** When mapping time to string formats for screen readers, always handle singular/plural strings (e.g., '1 minute') and omit the zero segments entirely for brevity and natural flow.
## 2024-05-19 - Wear OS Primary Action Chip Layout
**Learning:** Primary action buttons (like a "Start" Chip at the bottom of a configuration screen) that don't span the full width of the screen look unpolished and are harder to tap. Wear OS design guidelines favor large, full-width touch targets for primary bottom actions.
**Action:** Always apply `Modifier.fillMaxWidth()` to primary bottom-action `Chip` components to ensure they span the screen width and provide a clear, accessible touch target. Adding icons and secondary labels also reinforces intent and maintains consistency with other screens.
