package com.matt.packetlistener.helper;

import org.objectweb.asm.tree.ClassNode;

public interface IAsmTransformer {
    void transform(ClassNode node);
}
