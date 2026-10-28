{
  "schemaVersion": 1,
  "id": "@mod_id@",
  "version": "@mod_version@",
  "name": "@mod_name@",
  "description": "@description@",
  "authors": [
    "@authors@"
  ],
  "license": "@license@",
  "icon": "assets/enchantbookicons/icon.png",
  "environment": "client"@fabric_entrypoints@@fabric_mixins@,
  "depends": {
    "fabricloader": ">=@fabric_loader_min@",
    "minecraft": @fabric_mc_depends@,
    "java": ">=@java_version@"
  }
}
