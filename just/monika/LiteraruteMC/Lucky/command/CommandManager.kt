package just.monika.LiteraruteMC.Lucky.command

import just.monika.LiteraruteMC.Lucky.command.impl.*

object CommandManager {
    val commands = mutableSetOf<Command>()
    fun getCommandByName(name : String): Command? {
        commands.forEach{
            if("."+it.name==name) return it
        }
        return null
    }
    init {
        commands.let {
            it.add(Toggle)
            it.add(Bind)
        }
    }
}