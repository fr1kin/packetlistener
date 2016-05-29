package com.matt.packetlistener;

import com.matt.packetlistener.helper.IAsmTransformer;
import com.matt.packetlistener.patch.PatchNetManager;
import com.matt.packetlistener.patch.PatchNetManagerRun;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Map;

public class PacketTransformer implements IClassTransformer {
    private Map<String, IAsmTransformer> transformingClasses = new HashMap<String, IAsmTransformer>();

    public PacketTransformer() {
        transformingClasses.put("net.minecraft.network.NetworkManager", new PatchNetManager());
        transformingClasses.put("net.minecraft.network.NetworkManager$4", new PatchNetManagerRun());
    }

    @Override
    public byte[] transform(String name, String realName, byte[] bytes) {
        if(transformingClasses.containsKey(realName)) {
            try {
                PacketCoreMod.logger.info("Transforming class " + realName);

                ClassNode classNode = new ClassNode();
                ClassReader classReader = new ClassReader(bytes);
                classReader.accept(classNode, 0);

                transformingClasses.get(realName).transform(classNode);

                ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                classNode.accept(classWriter);

                // let gc clean this up
                transformingClasses.remove(realName);

                return classWriter.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
