package com.niteah.sapphire.network.packet

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PacketHandler (val id: String)