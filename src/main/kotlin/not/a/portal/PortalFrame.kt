package not.a.portal

import org.bukkit.block.Block

class PortalFrame(val block: Block) {

    private var validated = false
    private var valid = false

    val isNotValid: Boolean
        get() = isValid
    val isValid: Boolean
        get() {
            if (validated) {
                return valid
            } else {
                valid = validate()
                return valid
            }
        }

    private fun validate(): Boolean {
        validated = true
        return false
    }

    fun createPortal() {
        if (isNotValid) throw IllegalStateException("Portal can only be created for a valid portal frame")
    }
}