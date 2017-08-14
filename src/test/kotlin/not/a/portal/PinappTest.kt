package not.a.portal

import com.dumptruckman.bukkit.utils.PluginFactory
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class PinappTest {

    lateinit var pinapp: Pinapp

    @Before
    fun setUp() {
        pinapp = PluginFactory.createPluginInstance()
        pinapp.onLoad()
        pinapp.onEnable()
    }

    @Test
    fun testDefaultConfig() {
        val worldList = pinapp.config.getList("worlds")
        assertTrue(worldList.isNotEmpty())
        assertTrue(worldList[0] is WorldSettings)
    }
}