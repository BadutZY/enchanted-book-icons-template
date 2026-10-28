#!/usr/bin/env bash
# Membangun SEMUA profil (folder versions/) dan mengumpulkan jar ke dist/
# Pakai JDK 21 (JAVA_HOME). Contoh: ./build-all.sh              -> semua profil
#                                   ./build-all.sh 1.20.1 1.20.4 -> hanya profil tertentu
# Jika satu profil gagal, profil lain tetap dibangun; daftar yang gagal ditampilkan di akhir.
cd "$(dirname "$0")"
mkdir -p dist
if [ $# -eq 0 ]; then
  set -- $(ls versions/*.properties | xargs -n1 basename | sed 's/\.properties$//')
fi
failed=""
for p in "$@"; do
  echo "===== Build profil $p ====="
  ./gradlew dist -Pmc="$p" || failed="$failed $p"
done
echo
if [ -n "$failed" ]; then
  echo "Profil yang GAGAL:$failed"
  echo "Jar yang berhasil dibangun ada di folder dist/"
  exit 1
fi
echo "Selesai. Semua jar ada di folder dist/"
