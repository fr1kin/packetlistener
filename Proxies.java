package com.matt.packetlistener;

import com.matt.packetlistener.helper.AsmClass;
import com.matt.packetlistener.helper.AsmMethod;
import jdk.internal.org.objectweb.asm.Type;

/**
 * The truth is I couldn't think of any better name for this file
 */
public class Proxies {
    public static final Proxies INSTANCE = new Proxies();

    // MC classes and methods

    public AsmClass PACKET = new AsmClass()
            .setName("net/minecraft/network/Packet")
            .setObfuscatedName("fh");

    // Hook classes and methods
    public AsmMethod ON_SENDING_PACKET = new AsmMethod()
            .setName("onSendingPacket")
            .setParentClassName(Type.getInternalName(PacketCallbacks.class))
            .setArgumentTypes(PACKET)
            .setReturnType(boolean.class);

    public AsmMethod ON_SENT_PACKET = new AsmMethod()
            .setName("onSentPacket")
            .setParentClassName(Type.getInternalName(PacketCallbacks.class))
            .setArgumentTypes(PACKET)
            .setReturnType(void.class);

    public AsmMethod ON_PRE_RECEIVED = new AsmMethod()
            .setName("onPreReceived")
            .setParentClassName(Type.getInternalName(PacketCallbacks.class))
            .setArgumentTypes(PACKET)
            .setReturnType(boolean.class);

    public AsmMethod ON_POST_RECEIVED = new AsmMethod()
            .setName("onPostReceived")
            .setParentClassName(Type.getInternalName(PacketCallbacks.class))
            .setArgumentTypes(PACKET)
            .setReturnType(void.class);
}
