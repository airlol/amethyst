package com.niteah.amethyst.network.packet

import com.google.gson.JsonObject

interface Packet {

    fun getId(): String

    fun serialize(): JsonObject
    fun deserialize(obj: JsonObject)

}