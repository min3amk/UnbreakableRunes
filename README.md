# Unbreakable Runes — proof-of-concept source

Target: Minecraft 1.21.1 / NeoForge 21.1.248 / Ars Nouveau 5.11.3 / Java 21.

## What this prototype does

On entity join/load, it checks for the entity type `ars_nouveau:rune` and calls
`setInvulnerable(true)`. This is intended to stop damage from destroying the
Rune entity without cancelling its normal ticking or collision/touch behavior.

## Important limitations

- This is **source code**, not a compiled/tested `.jar`.
- It is **not yet a new Spell Book glyph**. It is a small addon prototype that
  automatically protects existing Ars Nouveau Rune entities. A true new glyph
  needs Ars Nouveau's exact 5.11.3 glyph registration and rune-placement API.
- The entity registry ID `ars_nouveau:rune` must be confirmed against the exact
  installed jar. If the actual Rune is represented differently, the ID check
  must be adjusted.
- `setInvulnerable(true)` protects against damage; it does not make the floor
  block indestructible or prevent unrelated Wither block destruction.
- Test in a copied world first. Do not put an unverified addon into the only
  copy of a world.

## Build

Requires Java 21 and Gradle 8.8+ (Gradle will download NeoForge build dependencies).
From this folder run:

```bash
gradle build
```

The output, if the build succeeds, will be in `build/libs/`.

Install the resulting jar in the `mods` folder on both client and server.
