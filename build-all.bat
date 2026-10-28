@echo off
REM Membangun SEMUA profil (folder versions\) dan mengumpulkan jar ke dist\
REM Pakai JDK 21 (JAVA_HOME). Contoh: build-all.bat            -> semua profil
REM                                   build-all.bat 1.20.1 1.20.4 -> hanya profil tertentu
REM Jika satu profil gagal, profil lain tetap dibangun; daftar yang gagal ditampilkan di akhir.
setlocal enabledelayedexpansion
if not exist dist mkdir dist
set FAILED=
if "%~1"=="" (
  for %%F in (versions\*.properties) do call :build %%~nF
) else (
  for %%P in (%*) do call :build %%P
)
echo.
if defined FAILED (
  echo Profil yang GAGAL:!FAILED!
  echo Jar yang berhasil dibangun ada di folder dist\
  exit /b 1
)
echo Selesai. Semua jar ada di folder dist\
exit /b 0

:build
echo ===== Build profil %1 =====
call gradlew.bat dist -Pmc=%1
if errorlevel 1 set FAILED=!FAILED! %1
exit /b 0
