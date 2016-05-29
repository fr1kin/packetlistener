package com.matt.packetlistener;

import com.matt.packetlistener.events.PacketEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

public class PacketCallbacks {
    public static final PacketCallbacks INSTANCE = new PacketCallbacks();

    public static boolean onSendingPacket(Packet<?> packet) {
        return MinecraftForge.EVENT_BUS.post(new PacketEvent.SentEvent.Pre(packet));
    }

    public static void onSentPacket(Packet<?> packet) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.SentEvent.Post(packet));
    }

    public static boolean onPreReceived(Packet<?> packet) {
        return MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceivedEvent.Pre(packet));
    }

    public static void onPostReceived(Packet<?> packet) {
        MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceivedEvent.Post(packet));
    }
}
