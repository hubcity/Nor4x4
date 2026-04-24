## $(date +%Y-%m-%d) - Jetpack Compose Box Accessibility
**Learning:** Custom clickable components created with `Box` and `Modifier.clickable` in Jetpack Compose for Wear OS do not automatically announce their role as buttons to screen readers, unlike the standard `Button` component.
**Action:** Always add `role = Role.Button` to `Modifier.clickable()` when building custom button-like elements using generic containers like `Box` or `Row`.

## $(date +%Y-%m-%d) - Jetpack Compose Semantic Merging and Natural Language Readouts
**Learning:** Timer formatting (like "04:00") is read poorly by TalkBack ("four colon zero zero"). Also, composite readouts (like a Row with "125" text and a Heart Icon) are read disjointedly. Wear OS users, especially during workouts, rely heavily on accurate, consolidated audio feedback.
**Action:** Use `Modifier.semantics { contentDescription = "..." }` to replace plain text with natural language phrases (e.g., "four minutes and zero seconds remaining"). Use `Modifier.semantics(mergeDescendants = true) { ... }` on parent layout components (like Row) to combine separate visual elements into a single coherent screen reader announcement.
## 2026-04-22 - Jetpack Compose Timer Formats and Progress Accessibility
**Learning:** Timer formatting (like "04:00") is read poorly by TalkBack ("four colon zero zero"). Furthermore, `CircularProgressIndicator` instances without semantic descriptions offer zero feedback to non-visual users during loading states.
**Action:** Use `Modifier.semantics { contentDescription = ... }` to map text values like "04:00" to natural language properties ("4 minutes and 0 seconds") and add loading descriptions to ambiguous indicators.
## 2026-04-24 - Improve TalkBack on Formatted Text

**Learning:** When using `Modifier.semantics { contentDescription = ... }` on a `Text` element that displays formatted visual information (like "04:30"), TalkBack will often read both the visual text *and* the content description redundantly.
**Action:** Use `Modifier.clearAndSetSemantics { contentDescription = ... }` instead on these elements to ensure only the natural language description (e.g., "4 minutes and 30 seconds remaining") is announced by screen readers.
