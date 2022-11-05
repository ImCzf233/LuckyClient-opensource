package just.monika.LiteraruteMC.Lucky.command

import just.monika.LiteraruteMC.Lucky.module.Module
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting

class ModuleCommand(var mod : Module) : Command(mod.name) {
    override fun onTyped(args: Array<String>) {
        try{
            mod.settingsList.forEach {
                if(it.name==args[0]){
                    if (it is NumberSetting){
                        it.value=args[1].toDouble()
                    }else if (it is BooleanSetting){
                        it.setState(args[1].toBoolean())
                    }
                }
            }
        }finally{}
    }
}