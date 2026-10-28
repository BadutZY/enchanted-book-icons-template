<div align="center">

<img src="common/src/main/resources/assets/enchantbookicons/icon.png" height="120">

# Enchanted Book Icons

**A client-side mod that gives every Enchanted Book its own icon**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.19%20to%2026.3-62B47A)](https://www.minecraft.net)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-DBD0B4)](https://fabricmc.net)
[![Forge](https://img.shields.io/badge/Loader-Forge-1E3A5F)](https://minecraftforge.net)
[![NeoForge](https://img.shields.io/badge/Loader-NeoForge-D97B2B)](https://neoforged.net)
[![Java](https://img.shields.io/badge/Java-17%20%7C%2021-ED8B00)](https://adoptium.net)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

</div>

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Supported Versions](#supported-versions)
- [How It Works](#how-it-works)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Building from Source](#building-from-source)
- [Configuration](#configuration)
- [Adding a New Enchantment](#adding-a-new-enchantment)
- [Adding a New Minecraft Version](#adding-a-new-minecraft-version)
- [Known Limitations](#known-limitations)
- [Project Status](#project-status)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

Enchanted Book Icons replaces the default Enchanted Book texture with a unique icon for each enchantment, so a book can be identified at a glance in inventories, chests, item frames, and dropped items. Books that are empty or hold more than one enchantment keep the vanilla texture.

The mod is purely visual and runs on the client only. It does not need to be installed on servers, does not change gameplay, and does not depend on Architectury API. The project uses Architectury Loom only as a build tool to produce Fabric, Forge, and NeoForge jars from one shared codebase.

---

## Key Features

- 43 enchantments with 43 distinct textures
- Client-side only, safe to join any server
- One codebase, three loaders: Fabric, Forge, and NeoForge
- Wide version coverage from Minecraft 1.19 up to 26.3
- Graceful fallback: if an internal Minecraft method changes, the vanilla texture is shown instead of crashing
- Metadata (`fabric.mod.json`, `mods.toml`, `neoforge.mods.toml`, `pack.mcmeta`) is generated from templates, so no per-version manual edits

---

## Supported Versions

Each build profile covers a range of Minecraft versions. Pick one jar that matches your Minecraft version and loader.

| Profile | Minecraft | Java | Loaders | Technique |
|---|---|---|---|---|
| `1.19.2` | 1.19 - 1.19.2 | 17 | Fabric, Forge | Code, NBT |
| `1.19.4` | 1.19.3 - 1.19.4 | 17 | Fabric, Forge | Code, NBT |
| `1.20.1` | 1.20 - 1.20.1 | 17 | Fabric, Forge, NeoForge (see note) | Code, NBT |
| `1.20.4` | 1.20.2 - 1.20.4 | 17 | Fabric, Forge, NeoForge | Code, NBT |
| `1.20.6` | 1.20.5 - 1.20.6 | 21 | Fabric, Forge, NeoForge | Code, data component |
| `1.21.1` | 1.21 - 1.21.3 | 21 | Fabric, Forge, NeoForge | Code, data component |
| `1.21.4` | 1.21.4 | 21 | Fabric, Forge, NeoForge | Mixin, `custom_model_data` |
| `1.21.5` | 1.21.5 - 26.3 | 21+ | Fabric, Forge, NeoForge | Data only, one universal jar |

Notes:

- NeoForge does not exist for Minecraft 1.19.x, so those profiles only provide Fabric and Forge jars.
- NeoForge 1.20.1 is a fork of Forge that still uses the `net.minecraftforge` packages, so the Forge jar runs on it as is. The build copies it as `enchantbookicons-neoforge-...jar` for convenience.

---

## How It Works

The technique depends on how each Minecraft version selects an item model.

**Minecraft 1.19 - 1.21.3 (item properties).** Item models use `overrides`. The mod registers an item property (`enchantbookicons:icon`) that reads the stored enchantment from the book and returns a number. The book model in `assets/minecraft/models/item/enchanted_book.json` maps each number to one texture. The enchantment is read from NBT on 1.19 - 1.20.4 and from data components on 1.20.5 and later.

**Minecraft 1.21.4 (mixin).** Mojang removed `overrides` in this version, and the `component` select property only arrives in 1.21.5. The mod therefore uses `ItemModelResolverMixin` to replace the rendered ItemStack with a client-side copy that carries a `custom_model_data` string equal to the enchantment name. A version-specific `items/enchanted_book.json` selects the model from that string. The real stack and the server are never touched. The mixin uses `require = 0`, so a changed method name only disables the icons.

**Minecraft 1.21.5 and later (data only).** The `minecraft:select` item model can match stored enchantments directly, so no code is needed. All loaders share one universal jar containing `fabric.mod.json`, `META-INF/mods.toml` (Forge, `lowcodefml`), and `META-INF/neoforge.mods.toml`.

---

## Installation

1. Install a supported loader (Fabric, Forge, or NeoForge) for your Minecraft version.
2. Download the jar that matches both your Minecraft version and loader.
3. Place the jar in the `mods` folder of your game directory.
4. Launch the game. No configuration is required.

Install only one jar per loader. The mod is not needed on the server.

---

## Project Structure

```
enchanted-book-icons-multiloader/
|- common/                          Shared code and assets
|  |- src/main/java/                EnchantBookIcons (init), BookIconOrder (enchantment list)
|  |- src/main/resources/assets/    Textures, models, items/enchanted_book.json
|  |- src/variants/
|     |- nbt/                       LegacyBookIcons for 1.19 - 1.20.4
|     |- component/                 LegacyBookIcons for 1.20.5 - 1.20.6
|     |- component_factory/         LegacyBookIcons for 1.21 - 1.21.3
|     |- mixin/                     Mixin, config, and items/ json for 1.21.4
|- fabric/                          Fabric entrypoint and build.gradle
|- forge/                           Forge entrypoint and build.gradle
|- neoforge/                        NeoForge entrypoint and build.gradle
|- metadata/                        Templates for mod metadata files
|- versions/                        One .properties file per build profile
|- tools/check_resources.py         Enchantment list consistency checker
|- dist/                            Built jars (generated)
|- build.gradle                     Profile loading, metadata rendering, dist task
|- settings.gradle                  Profile selection
|- build-all.bat / build-all.sh     Build every profile
```

---

## Building from Source

**Requirements**

- JDK 21 set as `JAVA_HOME` (Gradle 8.14 does not support JDK 25)
- Internet access on the first build, since Loom downloads and remaps Minecraft

Targets for Java 17 profiles are handled by the compiler `release` option, so JDK 21 is enough for every profile.

**Build one profile**

```bash
# Linux and macOS
./gradlew dist -Pmc=1.20.1

# Windows
gradlew.bat dist -Pmc=1.20.1
```

**Build all profiles**

```bash
# Every profile
./build-all.sh
build-all.bat

# Selected profiles only
./build-all.sh 1.20.1 1.21.4
```

If a profile fails, the remaining profiles still build and the failed ones are listed at the end.

**Output**

Jars are collected in `dist/`:

| Profile type | File name |
|---|---|
| Code profiles | `enchantbookicons-<loader>-<version>+<label>.jar` |
| Data-only profile | `enchantbookicons-universal-<version>+<label>.jar` |

The first build of each profile takes longer because Minecraft must be downloaded and remapped. If a build behaves oddly after changing versions or Loom, delete the `.gradle` folder in the project and try again.

---

## Configuration

**`versions/<profile>.properties`** defines one profile:

| Key | Purpose |
|---|---|
| `minecraft_version` | Minecraft version used for the dev environment |
| `variant` | Code variant: `nbt`, `component`, `component_factory`, `mixin`, or `data` |
| `java_version` | Java release target |
| `loaders` | Comma-separated loaders to build |
| `label` | Version label used in the jar name |
| `forge_version`, `neoforge_version` | Loader versions used to compile |
| `forge_major`, `forge_mc_range` | Forge loader version and Minecraft range written to `mods.toml` |
| `neoforge_min`, `neoforge_mc_range`, `neoforge_toml` | NeoForge metadata values |
| `fabric_loader_min`, `fabric_mc_depends` | Fabric metadata values |
| `pack_format` | Resource pack format for `pack.mcmeta` |

`forge_major` must be the lowest Forge major version in the profile range. For example, Forge 1.19 is version 41 and Forge 1.19.2 is version 43, so the 1.19.2 profile uses 41.

**`gradle.properties`** holds the build plugin versions (`loom_version`, `architectury_plugin_version`, `fabric_loader_version`) and the mod metadata (`mod_id`, `mod_version`, `mod_authors`, `mod_license`).

If Gradle reports `Could not find net.minecraftforge:forge:...` or `net.neoforged:neoforge:...`, replace the number in the profile with an existing release from the [Forge downloads](https://files.minecraftforge.net) or [NeoForge versions](https://projects.neoforged.net/neoforged/neoforge) page.

---

## Adding a New Enchantment

1. Append the enchantment name at the bottom of `ORDER` in `common/src/main/java/com/enchantbookicons/BookIconOrder.java`.
2. Create `textures/item/<name>.png` and `models/item/<name>.json` under `assets/enchantbookicons/`.
3. Add the entry to all three model files:
  - `overrides` in `assets/minecraft/models/item/enchanted_book.json`, with the value `(number - 0.5) / 100`
  - a case in `assets/minecraft/items/enchanted_book.json`
  - a case in `common/src/variants/mixin/resources/assets/minecraft/items/enchanted_book.json` (1.21.4)
4. Run `python3 tools/check_resources.py` to confirm everything matches.

---

## Adding a New Minecraft Version

- **Data-only (1.21.5 and later):** copy `versions/1.21.5.properties` and update `label`, `fabric_mc_depends`, `forge_mc_range`, `neoforge_mc_range`, and `pack_format`.
- **Code profile:** copy the closest existing profile and update `minecraft_version`, the loader versions, and the version ranges.

---

## Known Limitations

- The data-only path (1.21.5 and later) matches enchantment levels 1 to 10. Levels above 10 use the vanilla texture.
- On 1.19 - 1.20.6 the Sweeping Edge enchantment id is `minecraft:sweeping`, which is handled by an alias in `BookIconOrder`.
- The 43-enchantment list follows Minecraft 1.21.11. Enchantments that do not exist in an older version simply never appear.
- Jars are compiled against one version of each profile range, so behavior at the other end of the range depends on Minecraft internals staying compatible.

---

## Project Status

The project is under active development. Build profiles and loader versions are verified one at a time, and the 1.21.4 mixin path was recently reworked to remove the Fabric-only refmap so that Forge and NeoForge can apply it. If you find a build or runtime problem, please open an issue and include the full `latest.log` or the Gradle error output together with the Minecraft version and loader.

---

## Contributing

1. Fork this repository.
2. Create a branch: `git checkout -b feature/your-feature-name`
3. Make your changes and verify them with `python3 tools/check_resources.py` and a build of the affected profile.
4. Commit with a clear message and push the branch.
5. Open a Pull Request describing what changed and why.

Before opening a Pull Request, make sure that:

- Each new enchantment is added to every model file listed above
- Version ranges in `versions/*.properties` match the loader versions they target
- The affected jars were tested in game on the lowest and highest Minecraft version of the range

---

## License

This project is released under the [MIT License](LICENSE). Minecraft is a trademark of Mojang AB. This project is not affiliated with Mojang, Microsoft, or any mod loader project.

---

<div align="center">

**Enchanted Book Icons - Built with Architectury Loom for Fabric, Forge, and NeoForge**

*A client-side mod by BadutZY*

</div>