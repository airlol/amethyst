package com.niteah.sapphire.network

import com.niteah.sapphire.Sapphire
import com.niteah.sapphire.network.packet.PacketHandler
import redis.clients.jedis.JedisPubSub

class SapphirePubSub (val sapphire: Sapphire): JedisPubSub() {

    override fun onMessage(channel: String, message: String) {

        println(message)

        if (channel != sapphire.channel) return
        var obj = sapphire.parser.parse(message).asJsonObject
        var packet = sapphire.getPacket(obj)?: return

        for (listener in sapphire.listeners) {
            for (method in listener::class.java.declaredMethods) {
                if (!method.isAnnotationPresent(PacketHandler::class.java)) continue
                if (method.getAnnotation(PacketHandler::class.java).id != packet.getId()) continue
                method.invoke(listener, packet)
            }
        }

    }

}