package com.niteah.amethyst.network.packet

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PacketHandler (val id: String)