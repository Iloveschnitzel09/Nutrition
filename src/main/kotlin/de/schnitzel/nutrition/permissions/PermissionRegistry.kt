package de.schnitzel.nutrition.permissions

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "nutrition"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_NUTRITIONGUI = create("$COMMAND_PREFIX.nutritiongui")
    val COMMAND_TEST = create("$COMMAND_PREFIX.test")
}