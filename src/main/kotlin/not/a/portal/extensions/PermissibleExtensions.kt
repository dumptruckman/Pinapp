package not.a.portal.extensions

import org.bukkit.permissions.Permissible

infix fun Permissible.can(permission: String) = this.hasPermission(permission)
infix fun Permissible.cannot(permission: String) = !this.hasPermission(permission)