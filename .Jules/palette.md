## $(date +%Y-%m-%d) - Jetpack Compose Box Accessibility
**Learning:** Custom clickable components created with `Box` and `Modifier.clickable` in Jetpack Compose for Wear OS do not automatically announce their role as buttons to screen readers, unlike the standard `Button` component.
**Action:** Always add `role = Role.Button` to `Modifier.clickable()` when building custom button-like elements using generic containers like `Box` or `Row`.
