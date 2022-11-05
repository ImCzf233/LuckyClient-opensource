package just.monika.LiteraruteMC.Lucky.utils.toolkit

import org.reflections.Reflections
import java.lang.reflect.Modifier

object ClassUtils {

    inline fun <reified T> findClasses(
            pack: String,
            noinline block: Sequence<Class<out T>>.() -> Sequence<Class<out T>> = {
                filter { Modifier.isFinal(it.modifiers) }
            }
    ): List<Class<out T>> {
        return findClasses(pack, T::class.java, block)
    }
    @JvmStatic
    fun <T> findClasses0(
            pack: String,
            clazz: Class<T>,

    ): List<Class<out T>> {
        return findClasses(pack, clazz){
            filter { Modifier.isFinal(it.modifiers) }
        }
    }

    @JvmStatic
    fun <T> findClasses(
            pack: String,
            subType: Class<T>,
            block: Sequence<Class<out T>>.() -> Sequence<Class<out T>> = { this }
    ): List<Class<out T>> {
        return Reflections(pack).getSubTypesOf(subType).asSequence()
                .run(block)
                .sortedBy { it.simpleName }
                .toList()
    }

    @Suppress("UNCHECKED_CAST")
    val <T> Class<out T>.instance
        get() = this.getDeclaredField("INSTANCE")[null] as T
}