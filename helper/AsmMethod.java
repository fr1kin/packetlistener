package com.matt.packetlistener.helper;

import org.objectweb.asm.Type;

public class AsmMethod {
    private String methodName;
    private String methodObfuscatedName;
    private String argumentTypes;
    private String returnType;
    private String parentClassName;

    public AsmMethod() {
        methodName = methodObfuscatedName = argumentTypes = returnType = "";
    }

    public AsmMethod setName(String in) {
        methodName = in;
        return this;
    }
    public String getName() {
        return methodName;
    }

    public AsmMethod setObfuscatedName(String name) {
        methodObfuscatedName = name;
        return this;
    }
    public String getObfuscatedName() {
        return methodObfuscatedName;
    }

    public String getRuntimeName() {
        return (!AsmHelper.isMinecraftObfuscated() || methodName.isEmpty()) ? methodName : methodObfuscatedName;
    }

    public AsmMethod setArgumentTypes(Object... types) {
        StringBuilder builder = new StringBuilder();
        for(Object var : types) {
            builder.append(objectToDescriptor(var));
        }
        argumentTypes = builder.toString();
        return this;
    }
    public AsmMethod setReturnType(Object type) {
        returnType = objectToDescriptor(type);
        return this;
    }

    public String getDescriptor() {
        return String.format("(%s)%s", argumentTypes, returnType);
    }

    public AsmMethod setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
        return this;
    }
    public String getParentClassName() {
        return parentClassName;
    }

    private String objectToDescriptor(Object obj) {
        if (obj instanceof String)
            return (String)obj;
        else if (obj instanceof AsmClass) {
            return String.format("L%s;", ((AsmClass) obj).getRuntimeName());
        } else if(obj instanceof Class) {
            if(((Class) obj).isPrimitive()) {
                return Primitives.primitiveToDescriptor.get(obj).toString();
            } else {
                // Avoid using non-primitive classes, especially classes in net.minecraft
                // Importing them here will cause them to load too early
                AsmHelper.log().info("Warning: Non-primitive classes should not be used in TransformerMethod.setArgumentTypes");
                return String.format("L%s;", Type.getInternalName((Class) obj));
            }
        } else return "";
    }
}
