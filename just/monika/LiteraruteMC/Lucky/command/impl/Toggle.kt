package just.monika.LiteraruteMC.Lucky.command.impl

import just.monika.LiteraruteMC.Lucky.LuckyClient
import just.monika.LiteraruteMC.Lucky.command.Command

object Toggle : Command("t") {
    override fun onTyped(args: Array<String>) {
        try {
            LuckyClient.INSTANCE.moduleCollection.getModuleByName(args[0]).toggle()
        }catch (_:Throwable){

        }

    }
}