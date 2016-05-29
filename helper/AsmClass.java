package com.matt.packetlistener.helper;

public class AsmClass {
    private String name, obName;

    public AsmClass() {
        name = obName = "";
    }

    public AsmClass setName(String in) {
        name = in;
        return this;
    }
    public String getName() {
        return name;
    }

    public AsmClass setObfuscatedName(String name) {
        obName = name;
        return this;
    }
    public String getObfuscatedName() {
        return obName;
    }

    public String getRuntimeName() {
        return (!AsmHelper.isMinecraftObfuscated() || name.isEmpty()) ? name : obName;
    }
}
