package com.matt.packetlistener.patch;

import com.matt.packetlistener.PacketCoreMod;
import com.matt.packetlistener.Proxies;
import com.matt.packetlistener.helper.AsmHelper;
import com.matt.packetlistener.helper.AsmMethod;
import com.matt.packetlistener.helper.IAsmTransformer;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class PatchNetManager implements IAsmTransformer {
    private final AsmMethod DISPATCH_PACKET = new AsmMethod()
            .setName("dispatchPacket")
            .setObfuscatedName("a")
            .setArgumentTypes(Proxies.INSTANCE.PACKET, "[Lio/netty/util/concurrent/GenericFutureListener;")
            .setReturnType(void.class);

    private final AsmMethod CHANNEL_READ0 = new AsmMethod()
            .setName("channelRead0")
            .setObfuscatedName("a")
            .setArgumentTypes("Lio/netty/channel/ChannelHandlerContext;", Proxies.INSTANCE.PACKET)
            .setReturnType(void.class);

    public void transform(ClassNode node) {
        for(MethodNode method : node.methods) {
            if(method.name.equals(DISPATCH_PACKET.getRuntimeName()) &&
                    method.desc.equals(DISPATCH_PACKET.getDescriptor())) {
                PacketCoreMod.logger.info("Patching method dispatchPacket");
                patchDispatchPacket(method);
            } else if(method.name.equals(CHANNEL_READ0.getRuntimeName()) &&
                    method.desc.equals(CHANNEL_READ0.getDescriptor())) {
                PacketCoreMod.logger.info("Patching method channelRead0");
                patchChannelRead0(method);
            }
        }
    }

    private final int[] patternPreDispatch = new int[] {
            ALOAD, ALOAD, IF_ACMPEQ, ALOAD, INSTANCEOF, IFNE,
            0x00, 0x00,
            ALOAD, ALOAD, INVOKEVIRTUAL
    };

    private final int[] patternPostDispatch = new int[] {
            POP,
            0x00, 0x00,
            GOTO,
            0x00, 0x00, 0x00,
            ALOAD, GETFIELD, INVOKEINTERFACE, NEW, DUP
    };

    private void patchDispatchPacket(MethodNode method) {
        AbstractInsnNode preNode = AsmHelper.findPattern(method.instructions.getFirst(),
                patternPreDispatch, "xxxxxx??xxx");
        AbstractInsnNode postNode = AsmHelper.findPattern(method.instructions.getFirst(),
                patternPostDispatch, "x??x???xxxxx");

        if(preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();

            InsnList insnPre = new InsnList();
            insnPre.add(new VarInsnNode(ALOAD, 1));
            insnPre.add(new MethodInsnNode(INVOKESTATIC,
                    Proxies.INSTANCE.ON_SENDING_PACKET.getParentClassName(),
                    Proxies.INSTANCE.ON_SENDING_PACKET.getRuntimeName(),
                    Proxies.INSTANCE.ON_SENDING_PACKET.getDescriptor(),
                    false
            ));
            insnPre.add(new JumpInsnNode(IFNE, endJump));

            InsnList insnPost = new InsnList();
            insnPost.add(new VarInsnNode(ALOAD, 1));
            insnPost.add(new MethodInsnNode(INVOKESTATIC,
                    Proxies.INSTANCE.ON_SENT_PACKET.getParentClassName(),
                    Proxies.INSTANCE.ON_SENT_PACKET.getRuntimeName(),
                    Proxies.INSTANCE.ON_SENT_PACKET.getDescriptor(),
                    false
            ));
            insnPost.add(endJump);

            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insert(postNode, insnPost);
        } else {
            PacketCoreMod.logger.error("Failed to find nodes for dispatch packet");
        }
    }

    private final int[] patternPreSend = new int[] {
            ALOAD, ALOAD, GETFIELD, INVOKEINTERFACE
    };

    private final int[] patternPostSend = new int[] {
            INVOKEINTERFACE,
            0x00, 0x00,
            GOTO,
    };

    private void patchChannelRead0(MethodNode method) {
        AbstractInsnNode preNode = AsmHelper.findPattern(method.instructions.getFirst(),
                patternPreSend, "xxxx");
        AbstractInsnNode postNode = AsmHelper.findPattern(method.instructions.getFirst(),
                patternPostSend, "x??x");
        if(preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();

            InsnList insnPre = new InsnList();
            insnPre.add(new VarInsnNode(ALOAD, 2));
            insnPre.add(new MethodInsnNode(INVOKESTATIC,
                    Proxies.INSTANCE.ON_PRE_RECEIVED.getParentClassName(),
                    Proxies.INSTANCE.ON_PRE_RECEIVED.getRuntimeName(),
                    Proxies.INSTANCE.ON_PRE_RECEIVED.getDescriptor(),
                    false
            ));
            insnPre.add(new JumpInsnNode(IFNE, endJump));

            InsnList insnPost = new InsnList();
            insnPost.add(new VarInsnNode(ALOAD, 2));
            insnPost.add(new MethodInsnNode(INVOKESTATIC,
                    Proxies.INSTANCE.ON_POST_RECEIVED.getParentClassName(),
                    Proxies.INSTANCE.ON_POST_RECEIVED.getRuntimeName(),
                    Proxies.INSTANCE.ON_POST_RECEIVED.getDescriptor(),
                    false
            ));
            insnPost.add(endJump);

            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insert(postNode, insnPost);
        }
    }
}
