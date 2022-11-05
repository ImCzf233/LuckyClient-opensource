/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package just.monika.LiteraruteMC.Lucky.module.impl.combat

import just.monika.LiteraruteMC.Lucky.event.EventListener
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent
import just.monika.LiteraruteMC.Lucky.module.Category
import just.monika.LiteraruteMC.Lucky.module.Module
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting
import just.monika.LiteraruteMC.Lucky.utils.player.RotationUtils
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import java.util.Random


class KillAuraModule : Module(
        "ShitKillAura",
        Category.COMBAT,
        "杀戮光环~rua!"
){
    val random = Random()

    val range = NumberSetting("AuraRange", 3.2, 3.2, 0.1, 10.0)
    val fall = BooleanSetting("CriticalsFalling", true)

    init {
        this.addSettings(range,fall)
    }
    private val onPacketReceive = EventListener { playerTickEvent : TickEvent? ->
        run {
            val criticals: Boolean = fall.isEnabled
            val aurarange = range.value
            if (mc.thePlayer == null) {
                return@EventListener
            }
            for (targetez in mc.theWorld.playerEntities) {
                if (targetez !== mc.thePlayer) {
                    if (targetez.isInvisible) {
                        continue
                    }
                    if (mc.thePlayer.getDistance((targetez as Entity).posX,(targetez as Entity).posY,(targetez as Entity).posZ) <= aurarange && mc.thePlayer.fallDistance >= 0.1 && criticals) {
                        mc.thePlayer.rotationYaw = RotationUtils.getRotationsNeeded(targetez as Entity).get(0)
                        mc.thePlayer.rotationPitch = RotationUtils.getRotationsNeeded(targetez as Entity).get(1) + this.random.nextFloat() * 35.0f - 5.0f
                        mc.playerController.attackEntity(mc.thePlayer as EntityPlayer, targetez as Entity)
                        mc.thePlayer.swingItem()
                    } else {
                        if (mc.thePlayer.getDistance((targetez as Entity).posX,(targetez as Entity).posY,(targetez as Entity).posZ) > aurarange || criticals) {
                            continue
                        }
                        mc.thePlayer.rotationYaw = RotationUtils.getRotationsNeeded(targetez as Entity).get(0)
                        mc.thePlayer.rotationPitch = RotationUtils.getRotationsNeeded(targetez as Entity).get(1) + this.random.nextFloat() * 35.0f - 5.0f
                        mc.playerController.attackEntity(mc.thePlayer as EntityPlayer, targetez as Entity)
                        mc.thePlayer.swingItem()
                    }
                }
            }
        }
    }
}