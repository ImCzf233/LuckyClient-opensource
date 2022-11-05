package just.monika.LiteraruteMC.Lucky.command

abstract class Command(var name : String) {
    abstract fun onTyped(args : Array<String>)
}