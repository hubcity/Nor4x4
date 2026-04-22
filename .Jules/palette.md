## $(date +%Y-%m-%d) - Jetpack Compose Box Accessibility
**Learning:** Custom clickable components created with `Box` and `Modifier.clickable` in Jetpack Compose for Wear OS do not automatically announce their role as buttons to screen readers, unlike the standard `Button` component.
**Action:** Always add `role = Role.Button` to `Modifier.clickable()` when building custom button-like elements using generic containers like `Box` or `Row`.

## $(date +%Y-%m-%d) - Jetpack Compose Semantic Merging and Natural Language Readouts
**Learning:** Timer formatting (like "04:00") is read poorly by TalkBack ("four colon zero zero"). Also, composite readouts (like a Row with "125" text and a Heart Icon) are read disjointedly. Wear OS users, especially during workouts, rely heavily on accurate, consolidated audio feedback.
**Action:** Use `Modifier.semantics { contentDescription = "..." }` to replace plain text with natural language phrases (e.g., "four minutes and zero seconds remaining"). Use `Modifier.semantics(mergeDescendants = true) { ... }` on parent layout components (like Row) to combine separate visual elements into a single coherent screen reader announcement.
