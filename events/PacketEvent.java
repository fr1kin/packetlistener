package com.matt.packetlistener.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PacketEvent extends Event {
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packetIn) {
        packet = packetIn;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public static class SentEvent extends PacketEvent {
        public SentEvent(Packet<?> packetIn) {
            super(packetIn);
        }

        @Cancelable
        public static class Pre extends SentEvent {
            public Pre(Packet<?> packetIn) {
                super(packetIn);
            }
        }

        public static class Post extends SentEvent {
            public Post(Packet<?> packetIn) {
                super(packetIn);
            }
        }
    }

    public static class ReceivedEvent extends PacketEvent {
        public ReceivedEvent(Packet<?> packetIn) {
            super(packetIn);
        }

        @Cancelable
        public static class Pre extends ReceivedEvent {
            public Pre(Packet<?> packetIn) {
                super(packetIn);
            }
        }

        public static class Post extends ReceivedEvent {
            public Post(Packet<?> packetIn) {
                super(packetIn);
            }
        }
    }

}
