/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package just.monika.LiteraruteMC.Lucky

import cn.enaium.cf4m.CF4M
import just.monika.LiteraruteMC.Lucky.config.ConfigManager
import just.monika.LiteraruteMC.Lucky.config.DragManager
import just.monika.LiteraruteMC.Lucky.event.Event
import just.monika.LiteraruteMC.Lucky.event.EventProtocol
import just.monika.LiteraruteMC.Lucky.module.Module
import just.monika.LiteraruteMC.Lucky.module.ModuleCollection
import just.monika.LiteraruteMC.Lucky.ui.altmanager.AltPanels
import just.monika.LiteraruteMC.Lucky.ui.altmanager.GuiAltManager
import just.monika.LiteraruteMC.Lucky.ui.altmanager.helpers.KingGenApi
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager
import just.monika.LiteraruteMC.Lucky.utils.Utils
import just.monika.LiteraruteMC.Lucky.utils.client.ReleaseType
import just.monika.LiteraruteMC.Lucky.utils.objects.Dragging
import org.apache.logging.log4j.LogManager
import java.awt.Color
import java.io.File
import java.util.concurrent.Executors

enum class LuckyClient : Utils {
    INSTANCE;


    val eventProtocol =
        EventProtocol<Event>()
    val notificationManager = NotificationManager()
    val executorService = Executors.newSingleThreadExecutor()
    var moduleCollection = ModuleCollection()
    var configManager = ConfigManager()

    //    public CommandHandler getCommandHandler() { return commandHandler; }
    var altManager: GuiAltManager? = null

    @JvmField
    val altPanels = AltPanels()
    @JvmField
    var kingGenApi: KingGenApi? = null
    val version: String
        get() = VERSION + if (RELEASE != ReleaseType.PUBLIC) " (" + RELEASE.getName() + ")" else ""
    val clientColor: Color
        get() = Color(236, 133, 209)
    val alternateClientColor: Color
        get() = Color(28, 167, 222)

    fun isToggled(c: Class<out Module?>?): Boolean {
        val m = moduleCollection[c]
        return m != null && m.isToggled
    }

    fun createDrag(module: Module?, name: String?, x: Float, y: Float): Dragging? {
        DragManager.draggables[name] = Dragging(module, name, x, y)
        return DragManager.draggables[name]
    }

    companion object {
        const val NAME = "LuckyClient"
        const val VERSION = "1.0"
        val RELEASE = ReleaseType.DEV
        @JvmField
        val LOGGER = LogManager.getLogger(NAME)
        @JvmField
        val DIRECTORY = File(Utils.mc.mcDataDir, "LuckyClient")

        init {
            LOGGER.debug("-> Load")
            FuckVapu.start()
        }
    }

    init {
        CF4M.run(LuckyClient::javaClass)
    }
}