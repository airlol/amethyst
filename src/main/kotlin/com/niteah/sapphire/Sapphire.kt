package com.niteah.sapphire

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.niteah.sapphire.network.SapphirePubSub
import com.niteah.sapphire.network.packet.Packet
import com.niteah.sapphire.network.packet.PacketListener
import redis.clients.jedis.JedisPool

class Sapphire (val channel: String, host: String = "127.0.0.1", port: Int = 6379, pass: String = "") {

    var parser: JsonParser = JsonParser()

    var pubPool: JedisPool = JedisPool(host, port)
    var subPool: JedisPool = JedisPool(host, port)

    var listeners = mutableListOf<PacketListener>()
    private var packets = mutableMapOf<String, Packet>()

    init {
        if (pass != "") pubPool.resource.auth(pass)
        if (pass != "") subPool.resource.auth(pass)

        Thread {
            subPool.resource.subscribe(SapphirePubSub(this), channel)
        }.start()
    }

    fun pub(packet: Packet) {
        var obj = packet.serialize()
        obj.addProperty("id", packet.getId())

        Thread {
            pubPool.resource.publish(channel, obj.toString())
        }.start()
    }

    fun getPacket(obj: JsonObject): Packet? {
        var packet = packets[obj["id"].asString]?: return null
        obj.remove("id")
        packet.deserialize(obj)

        return packet
    }

    fun close() {
        pubPool.close()
        subPool.close()
    }

    fun register(listener: PacketListener) {
        listeners.add(listener)
    }

    fun register(packet: Packet) {
        packets[packet.getId()] = packet
    }

}