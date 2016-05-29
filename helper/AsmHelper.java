package com.matt.packetlistener.helper;

import com.matt.packetlistener.PacketCoreMod;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;

import static org.objectweb.asm.Opcodes.*;

public class AsmHelper {

    public static Logger log() {
        return PacketCoreMod.logger;
    }

    public static boolean isMinecraftObfuscated() {
        return PacketCoreMod.isObfuscated;
    }

    /**
     * Prints all the opcodes from the start of the node to the end of the node
     * @param start starting point
     */
    public static void outputBytecode(AbstractInsnNode start) {
        StringBuilder hexPattern = new StringBuilder(), mask = new StringBuilder(), deciPattern = new StringBuilder();
        AbstractInsnNode next = start;
        do {
            hexPattern.append("0x");
            if(next.getOpcode() != F_NEW) {
                mask.append("x");
                hexPattern.append(Integer.toHexString(next.getOpcode()));
                deciPattern.append(String.format("%4d", next.getOpcode()));
            } else {
                mask.append("?");
                hexPattern.append("00");
                deciPattern.append("NULL");
            }
            hexPattern.append("\\");
            deciPattern.append(", ");
            next = next.getNext();
        } while(next != null);

        log().info("Pattern (base10): " + deciPattern.toString());
        log().info("Pattern (base16): " + hexPattern.toString());
        log().info("Mask: " + mask.toString());
    }

    /**
     * Breaks a string of hexadecimal opcodes into a base10 integer array
     * @param pattern pattern
     * @return Integer array
     */
    public static int[] convertPattern(String pattern) {
        if(pattern.startsWith("\0"))
            pattern = pattern.substring(1);
        String[] hex = pattern.split("\0");
        int[] buff = new int[hex.length];
        int index = 0;
        for(String number : hex) {
            if(number.startsWith("0x"))
                number = number.substring(2);
            else if(number.startsWith("x"))
                number = number.substring(1);
            buff[index++] = Integer.parseInt(number, 16);
        }
        return buff;
    }

    /**
     * Finds a pattern of opcodes and returns the first node of the matched pattern if found
     * @param start starting node
     * @param pattern integer array of opcodes
     * @param mask same length as the pattern. 'x' indicates the node will be checked, '?' indicates the node will be skipped over (has a bad opcode)
     * @return top node of matching pattern or null if nothing is found
     */
    public static AbstractInsnNode findPattern(AbstractInsnNode start, int[] pattern, char[] mask) {
        if(start != null &&
                pattern.length == mask.length) {
            int found = 0;
            AbstractInsnNode next = start;
            do {
                switch(mask[found]) {
                    // Analyze this node
                    case 'x': {
                        // Check if node and pattern have same opcode
                        if(next.getOpcode() == pattern[found]) {
                            // Increment number of matched opcodes
                            found++;
                        } else {
                            // Go back to the starting node
                            for(int i = 1; i <= (found - 1); i++) {
                                next = next.getPrevious();
                            }
                            // Reset the number of opcodes found
                            found = 0;
                        }
                        break;
                    }
                    // Skips over this node
                    default:
                    case '?':
                        found++;
                        break;
                }
                // Check if found entire pattern
                if(found >= mask.length) {
                    // Go back to top node
                    for(int i = 1; i <= (found - 1); i++)
                        next = next.getPrevious();
                    return next;
                }
                next = next.getNext();
            } while(next != null &&
                    found < mask.length);
        }
        log().info("Failed to match pattern");
        return null;
    }
    public static AbstractInsnNode findPattern(AbstractInsnNode start, String pattern, String mask) {
        return findPattern(start,
                convertPattern(pattern),
                mask.toCharArray());
    }
    public static AbstractInsnNode findPattern(AbstractInsnNode start, int[] pattern, String mask) {
        return findPattern(start,
                pattern,
                mask.toCharArray());
    }
}
