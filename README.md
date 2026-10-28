# Enchanted Book Icons (Fabric + Forge + NeoForge, Minecraft 1.19 - 26.3, termasuk 1.21.4)

Mod client-side: setiap Enchanted Book yang berisi tepat 1 enchantment punya texture sendiri
(43 enchantment, 43 texture berbeda). Buku kosong atau berisi lebih dari 1 enchantment memakai texture bawaan.

> **Status verifikasi (jujur):** proyek ini belum pernah di-build dan dijalankan di Minecraft.
> Yang sudah dicek: semua JSON/TOML hasil template valid untuk setiap profil, daftar enchantment
> konsisten (`tools/check_resources.py`), dan kode Java lolos kompilasi terhadap stub API.
> Yang BELUM dicek: build Gradle sesungguhnya, nomor versi Forge/NeoForge (lihat di bawah), dan
> perilaku di game (termasuk mixin 1.21.4, yang nama method-nya saya tulis dari ingatan). Kalau ada error saat build, kirim lognya.

## Soal "Architectury API"
Proyek ini memakai **Architectury Loom + Architectury Plugin** (struktur `common` + `fabric` +
`forge` + `neoforge`), tapi **tidak** memakai Architectury API sebagai dependensi runtime. Mod ini
hanya butuh `ItemProperties.register(...)` yang sudah publik di Minecraft, jadi pemain tidak perlu
memasang Architectury API. Kalau suatu saat butuh fitur API-nya, tambahkan di `build.gradle` modul
`common` dan tiap loader.

## Profil build (satu `-Pmc=<profil>` = satu kelompok versi)

| Profil | Minecraft | Java | Loader | Cara kerja |
|--------|-----------|------|--------|------------|
| `1.19.2` | 1.19 - 1.19.2 | 17 | Fabric, Forge | Kode, enchantment dari NBT |
| `1.19.4` | 1.19.3 - 1.19.4 | 17 | Fabric, Forge | Kode, NBT |
| `1.20.1` | 1.20 - 1.20.1 | 17 | Fabric, Forge | Kode, NBT |
| `1.20.4` | 1.20.2 - 1.20.4 | 17 | Fabric, Forge, NeoForge | Kode, NBT |
| `1.20.6` | 1.20.5 - 1.20.6 | 21 | Fabric, Forge, NeoForge | Kode, data component |
| `1.21.1` | 1.21 - 1.21.3 | 21 | Fabric, Forge, NeoForge | Kode, data component |
| `1.21.4` | **1.21.4** | 21 | Fabric, Forge, NeoForge | **Mixin** + `custom_model_data` (lihat bawah) |
| `1.21.5` | 1.21.5 - 26.3 | 21+ | semua (1 jar universal) | Murni data |

- **1.21.4 (baru).** Mojang menghapus `overrides` di 1.21.4, tetapi select `component` baru ada
  di 1.21.5, jadi tidak ada cara murni data. Solusinya: `ItemModelResolverMixin` mengganti
  ItemStack buku (hanya saat dirender, hanya di client) dengan salinan yang membawa
  `custom_model_data` string = nama enchantment, lalu `items/enchanted_book.json` khusus 1.21.4
  memilih model dari string itu. Kalau nama method internal Minecraft ternyata berbeda, mixin
  dilewati (`require = 0`) dan buku tampil dengan texture bawaan, tidak crash.
- Profil murni data (1.21.5 - 26.3) digabung jadi SATU jar universal: tanpa kode, tanpa
  Loom, berisi `fabric.mod.json` + `META-INF/mods.toml` (Forge, `lowcodefml`) +
  `META-INF/neoforge.mods.toml`. Jar yang sama dipakai di Fabric, Forge, dan NeoForge, dari
  1.21.5 sampai 26.3.
- Perbaikan 1.4.0 -> 1.5.0: di 1.19 - 1.20.6 id enchantment Sweeping Edge masih
  `minecraft:sweeping` (baru jadi `sweeping_edge` di 1.21). Sebelumnya buku Sweeping Edge di
  versi itu jatuh ke texture bawaan; sekarang ada alias di `BookIconOrder`.
- Pilih SATU jar sesuai versi Minecraft dan loader Anda.

## Cara build
1. Pasang **JDK 21** dan jadikan `JAVA_HOME` (Gradle 8.14 belum mendukung JDK 25; kode 26.x tidak
   perlu JDK 25 untuk dibuild karena tidak ada yang dikompilasi).
2. Satu profil: `./gradlew dist -Pmc=1.20.1` (Windows: `gradlew.bat dist -Pmc=1.20.1`)
3. Semua profil: `./build-all.sh` atau `build-all.bat`. Sebagian saja: `./build-all.sh 1.20.1 26.1`.

Jar hasilnya ada di `dist/`:
`enchantbookicons-fabric-<versi>+<label>.jar`, `...-forge-...jar`, `...-neoforge-...jar`
(profil berkode) atau `enchantbookicons-universal-<versi>+<label>.jar` (profil murni data).
Build pertama tiap profil lama karena Loom mengunduh dan me-remap Minecraft.

## Versi yang perlu Anda cek
Nomor versi dev environment ada di `versions/*.properties`. Yang BARU ditambahkan dan belum dicek: `forge_version` dan `neoforge_version` profil 1.21.4 (`1.21.4-54.1.0` dan `21.4.140`). Yang sudah saya cek ada di maven/situs
resmi: Forge `1.19.2-43.5.1`, Forge 1.21.3 terbaru `53.1.16`, Forge `1.20.1-47.4.0` (ada).
Yang saya isi dari ingatan dan BELUM dicek: `forge_version` di profil 1.19.4, 1.20.4, 1.20.6,
1.21.1 dan semua `neoforge_version`. Kalau Gradle bilang `Could not find net.minecraftforge:forge:...`
atau `net.neoforged:neoforge:...`, ganti angkanya dengan build yang ada di:
- Forge: https://files.minecraftforge.net
- NeoForge: https://projects.neoforged.net/neoforged/neoforge

Plugin build: `loom_version`, `architectury_plugin_version`, `fabric_loader_version` ada di
`gradle.properties`. Wrapper dipatok ke Gradle 8.14.3 karena kombinasi Architectury Loom 1.11 +
Architectury Plugin 3.4 itulah yang saya kenal. Kalau Anda ingin Loom yang lebih baru (mis. 1.13),
naikkan `loom_version` dan cek kompatibilitas Gradle-nya.

## Struktur
```
common/        kode + asset bersama (texture, model, items/enchanted_book.json)
  src/main/java/                 EnchantBookIcons (init), BookIconOrder (daftar enchantment)
  src/variants/<varian>/java/    LegacyBookIcons.java per kelompok versi (nbt, component, component_factory, mixin)
  src/variants/mixin/resources/  mixin config + items/enchanted_book.json khusus 1.21.4
fabric/ forge/ neoforge/         entrypoint tipis + build.gradle tiap loader
metadata/      template fabric.mod.json, mods.toml, neoforge.mods.toml, pack.mcmeta
versions/      satu file per profil (versi MC, Java, loader, rentang versi)
tools/check_resources.py         pemeriksa konsistensi daftar enchantment
```
Metadata dibuat otomatis dari `metadata/*.tpl` + `versions/<profil>.properties`, jadi tidak ada
file metadata yang perlu diedit tangan per versi.

## Menambah enchantment baru
1. Tambahkan nama di `ORDER` (paling bawah) pada `common/.../BookIconOrder.java`.
2. Buat `textures/item/<nama>.png` dan `models/item/<nama>.json`.
3. Tambahkan 1 entry di `overrides` pada `assets/minecraft/models/item/enchanted_book.json`
   (nilai `(nomor - 0.5) / 100`) dan 1 case di `assets/minecraft/items/enchanted_book.json` serta di
   `common/src/variants/mixin/resources/assets/minecraft/items/enchanted_book.json` (1.21.4).
4. Jalankan `python3 tools/check_resources.py` untuk memastikan semuanya cocok.

## Menambah versi Minecraft baru
- Murni data (1.21.5+ / 26.x): salin `versions/1.21.5.properties`, ubah `label`, `fabric_mc_depends`,
  `forge_mc_range`, `neoforge_mc_range`, `pack_format`.
- Berkode: salin profil terdekat, ubah `minecraft_version`, versi loader, dan rentang versi.

## Batasan yang diwarisi dari versi Fabric-only
- Jalur data (1.21.5+) mencocokkan level 1 - 10 per enchantment. Level di atas 10 memakai
  texture bawaan.
- Daftar 43 enchantment mengikuti 1.21.11; enchantment yang belum ada di suatu versi tidak pernah
  muncul sehingga texture-nya tidak terpakai.
