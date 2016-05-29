package com.matt.packetlistener.patch;

import com.matt.packetlistener.PacketCoreMod;
import com.matt.packetlistener.Proxies;
import com.matt.packetlistener.helper.AsmClass;
import com.matt.packetlistener.helper.AsmHelper;
import com.matt.packetlistener.helper.AsmMethod;
import com.matt.packetlistener.helper.IAsmTransformer;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class PatchNetManagerRun implements IAsmTransformer {
    private final AsmClass NETWORK_MANAGER$4 = new AsmClass()
            .setName("net/minecraft/network/NetworkManager$4")
            .setObfuscatedName("em$4");

    private final AsmMethod RUN = new AsmMethod()
            .setName("run")
            .setObfuscatedName("run")
            .setArgumentTypes()
            .setReturnType(void.class);

    @Override
    public void transform(ClassNode node) {
        for(MethodNode method : node.methods) {
            if(method.name.equals(RUN.getRuntimeName()) &&
                    method.desc.equals(RUN.getDescriptor())) {
                PacketCoreMod.logger.info("Patching method NetworkManager$4.run");
                patchRun(method);
            }
        }
    }

    private final int[] patternPreDispatch = new int[] {
            ALOAD, GETFIELD, ALOAD, GETFIELD, IF_ACMPEQ
    };

    private final int[] patternPostDispatch = new int[] {
            RETURN
    };

    private void patchRun(MethodNode method) {
        AbstractInsnNode preNode = AsmHelper.findPattern(method.instructions.getFirst(),
                patternPreDispatch, "xxxxx");
        AbstractInsnNode postNode = AsmHelper.findPattern(method.instructions.getFirst(),
                patternPostDispatch, "x");

        if(preNode != null && postNode != null) {
            LabelNode endJump = new LabelNode();

            InsnList insnPre = new InsnList();
            insnPre.add(new VarInsnNode(ALOAD, 0));
            insnPre.add(new FieldInsnNode(GETFIELD,
                    NETWORK_MANAGER$4.getRuntimeName(),
                    "val$inPacket",
                    String.format("L%s;", Proxies.INSTANCE.PACKET.getRuntimeName())
            ));
            insnPre.add(new MethodInsnNode(INVOKESTATIC,
                    Proxies.INSTANCE.ON_SENDING_PACKET.getParentClassName(),
                    Proxies.INSTANCE.ON_SENDING_PACKET.getRuntimeName(),
                    Proxies.INSTANCE.ON_SENDING_PACKET.getDescriptor(),
                    false
            ));
            insnPre.add(new JumpInsnNode(IFNE, endJump));

            InsnList insnPost = new InsnList();
            insnPost.add(new VarInsnNode(ALOAD, 0));
            insnPost.add(new FieldInsnNode(GETFIELD,
                    NETWORK_MANAGER$4.getRuntimeName(),
                    "val$inPacket",
                    String.format("L%s;", Proxies.INSTANCE.PACKET.getRuntimeName())
            ));
            insnPost.add(new MethodInsnNode(INVOKESTATIC,
                    Proxies.INSTANCE.ON_SENT_PACKET.getParentClassName(),
                    Proxies.INSTANCE.ON_SENT_PACKET.getRuntimeName(),
                    Proxies.INSTANCE.ON_SENT_PACKET.getDescriptor(),
                    false
            ));
            insnPost.add(endJump);

            method.instructions.insertBefore(preNode, insnPre);
            method.instructions.insertBefore(postNode, insnPost);
        } else {
            PacketCoreMod.logger.error("Failed to find nodes for dispatch packet");
        }
    }
}
