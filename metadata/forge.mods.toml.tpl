modLoader="@forge_mod_loader@"
loaderVersion="@forge_loader_range@"
license="@license@"
clientSideOnly=true

[[mods]]
modId="@mod_id@"
version="@mod_version@"
displayName="@mod_name@"
authors="@authors@"
description='''@description@'''
logoFile="assets/enchantbookicons/icon.png"
# Mod client-only: boleh join server yang tidak punya mod ini (Forge versi lama membaca displayTest).
displayTest="IGNORE_ALL_VERSION"

[[dependencies.@mod_id@]]
modId="minecraft"
mandatory=true
type="required"
versionRange="@forge_mc_range@"
ordering="NONE"
side="CLIENT"
@toml_mixins@