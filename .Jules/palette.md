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
