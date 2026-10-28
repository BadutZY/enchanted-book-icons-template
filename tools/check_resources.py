#!/usr/bin/env python3
"""Memeriksa konsistensi daftar enchantment di semua tempat.

Jalankan dari root proyek:  python3 tools/check_resources.py

Yang diperiksa:
  1. ORDER di BookIconOrder.java  <->  "overrides" di models/item/enchanted_book.json
     (urutan, nilai ambang (nomor - 0.5) / 100, dan nama model)
  2. items/enchanted_book.json punya 1 case per enchantment, dengan model yang benar
  3. Setiap enchantment punya file model dan texture
  4. Semua JSON valid
"""
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
RES = ROOT / "common/src/main/resources/assets"
MOD = RES / "enchantbookicons"
errors = []


def err(msg):
    errors.append(msg)


src = (ROOT / "common/src/main/java/com/enchantbookicons/BookIconOrder.java").read_text(encoding="utf-8")
block = re.search(r"List\.of\((.*?)\);", src, re.S).group(1)
order = re.findall(r'"([a-z_]+)"', block)
print(f"ORDER: {len(order)} enchantment")
if len(set(order)) != len(order):
    err("ORDER berisi duplikat")

overrides = json.loads((RES / "minecraft/models/item/enchanted_book.json").read_text(encoding="utf-8"))["overrides"]
if len(overrides) != len(order):
    err(f"jumlah overrides ({len(overrides)}) != jumlah ORDER ({len(order)})")
for i, (name, ov) in enumerate(zip(order, overrides), start=1):
    want = round((i - 0.5) / 100, 6)
    got = round(ov["predicate"]["enchantbookicons:icon"], 6)
    if want != got:
        err(f"override #{i} ({name}): ambang {got}, seharusnya {want}")
    if ov["model"] != f"enchantbookicons:item/{name}":
        err(f"override #{i}: model {ov['model']} tidak cocok dengan {name}")

items = json.loads((RES / "minecraft/items/enchanted_book.json").read_text(encoding="utf-8"))["model"]
cases = items["cases"]
if len(cases) != len(order):
    err(f"jumlah case items/ ({len(cases)}) != jumlah ORDER ({len(order)})")
for name, case in zip(order, cases):
    if case["model"]["model"] != f"enchantbookicons:item/{name}":
        err(f"items/ case {name}: model salah ({case['model']['model']})")
    keys = {k for w in case["when"] for k in w}
    if keys != {f"minecraft:{name}"}:
        err(f"items/ case {name}: kunci 'when' tidak cocok: {keys}")

# items/ varian mixin (Minecraft 1.21.4): select custom_model_data string index 0
mixin_items = json.loads((ROOT / "common/src/variants/mixin/resources/assets/minecraft/items/enchanted_book.json")
                         .read_text(encoding="utf-8"))["model"]
if mixin_items.get("property") != "minecraft:custom_model_data" or mixin_items.get("index") != 0:
    err("items/ 1.21.4: harus select minecraft:custom_model_data index 0")
if [c["when"] for c in mixin_items["cases"]] != order:
    err("items/ 1.21.4: urutan/nama case tidak sama dengan ORDER")
for c in mixin_items["cases"]:
    if c["model"]["model"] != f"enchantbookicons:item/{c['when']}":
        err(f"items/ 1.21.4 case {c['when']}: model salah")
if "sweeping_edge" not in order or '"minecraft:sweeping"' not in src:
    err("alias minecraft:sweeping -> sweeping_edge (1.19 - 1.20.6) hilang")

for name in order:
    m = MOD / "models/item" / f"{name}.json"
    t = MOD / "textures/item" / f"{name}.png"
    if not m.exists():
        err(f"model hilang: {m.relative_to(ROOT)}")
    else:
        if json.loads(m.read_text(encoding="utf-8"))["textures"]["layer0"] != f"enchantbookicons:item/{name}":
            err(f"{m.name}: layer0 salah")
    if not t.exists():
        err(f"texture hilang: {t.relative_to(ROOT)}")

extra = {p.stem for p in (MOD / "textures/item").glob("*.png")} - set(order)
if extra:
    err(f"texture tanpa entri di ORDER: {sorted(extra)}")

if errors:
    print("\nDITEMUKAN MASALAH:")
    for e in errors:
        print(" -", e)
    sys.exit(1)
print("OK: semua konsisten.")
