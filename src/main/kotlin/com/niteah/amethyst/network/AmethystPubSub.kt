package com.niteah.amethyst.network

import com.niteah.amethyst.network.packet.PacketHandler
import com.niteah.amethyst.Amethyst
import redis.clients.jedis.JedisPubSub

class AmethystPubSub (private val amethyst: Amethyst): JedisPubSub() {

    override fun onMessage(channel: String, message: String) {

        println(message)

        if (channel != amethyst.channel) return
        var obj = amethyst.parser.parse(message).asJsonObject
        var packet = amethyst.getPacket(obj)?: return

        for (listener in amethyst.listeners) {
            for (method in listener::class.java.declaredMethods) {
                if (!method.isAnnotationPresent(PacketHandler::class.java)) continue
                if (method.getAnnotation(PacketHandler::class.java).id != packet.getId()) continue
                method.invoke(listener, packet)
            }
        }

    }

}