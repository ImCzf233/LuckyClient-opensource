package just.monika.LiteraruteMC.Lucky.command.impl

import just.monika.LiteraruteMC.Lucky.LuckyClient
import just.monika.LiteraruteMC.Lucky.command.Command
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationType
import org.lwjgl.input.Keyboard

object Bind : Command("bind") {
    override fun onTyped(args: Array<String>) {
        try {
            LuckyClient.INSTANCE.moduleCollection.getModuleByName(args[0]).setKey(Keyboard.getKeyIndex(args[1]))
            NotificationManager.post(NotificationType.SUCCESS, "Bind ${args[0]} to ${args[1]} success.", "LoL")
        }catch (_:Throwable){}
    }

}