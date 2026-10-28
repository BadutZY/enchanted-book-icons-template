modLoader="@neoforge_mod_loader@"
loaderVersion="[1,)"
license="@license@"

[[mods]]
modId="@mod_id@"
version="@mod_version@"
displayName="@mod_name@"
authors="@authors@"
description='''@description@'''
logoFile="assets/enchantbookicons/icon.png"

[[dependencies.@mod_id@]]
modId="neoforge"
mandatory=true
type="required"
versionRange="[@neoforge_min@,)"
ordering="NONE"
side="CLIENT"

[[dependencies.@mod_id@]]
modId="minecraft"
mandatory=true
type="required"
versionRange="@neoforge_mc_range@"
ordering="NONE"
side="CLIENT"
@toml_mixins@