package com.matt.packetlistener.helper;

import java.util.HashMap;
import java.util.Map;

public class Primitives {
    public final static Map<Class<?>, Class<?>> primitiveToWrapper = new HashMap<Class<?>, Class<?>>();
    public final static Map<Class<?>, Class<?>> wrapperToPrimitive = new HashMap<Class<?>, Class<?>>();
    public final static Map<Class<?>, Character> primitiveToDescriptor = new HashMap<Class<?>, Character>();

    static {
        primitiveToWrapper.put(boolean.class, Boolean.class);
        primitiveToWrapper.put(byte.class, Byte.class);
        primitiveToWrapper.put(short.class, Short.class);
        primitiveToWrapper.put(char.class, Character.class);
        primitiveToWrapper.put(int.class, Integer.class);
        primitiveToWrapper.put(long.class, Long.class);
        primitiveToWrapper.put(float.class, Float.class);
        primitiveToWrapper.put(double.class, Double.class);
        primitiveToWrapper.put(void.class, Void.class);

        wrapperToPrimitive.put(Boolean.class, boolean.class);
        wrapperToPrimitive.put(Byte.class, byte.class);
        wrapperToPrimitive.put(Short.class, short.class);
        wrapperToPrimitive.put(Character.class, char.class);
        wrapperToPrimitive.put(Integer.class, int.class);
        wrapperToPrimitive.put(Long.class, long.class);
        wrapperToPrimitive.put(Float.class, float.class);
        wrapperToPrimitive.put(Double.class, double.class);
        wrapperToPrimitive.put(Void.class, void.class);

        primitiveToDescriptor.put(boolean.class, 'Z');
        primitiveToDescriptor.put(byte.class, 'B');
        primitiveToDescriptor.put(short.class, 'S');
        primitiveToDescriptor.put(char.class, 'C');
        primitiveToDescriptor.put(int.class, 'I');
        primitiveToDescriptor.put(long.class, 'J');
        primitiveToDescriptor.put(float.class, 'F');
        primitiveToDescriptor.put(double.class, 'D');
        primitiveToDescriptor.put(void.class, 'V');
    }
}
