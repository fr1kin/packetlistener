package com.matt.packetlistener;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@IFMLLoadingPlugin.SortingIndex(1001)
public class PacketCoreMod implements IFMLLoadingPlugin {
    public static boolean isObfuscated = false;
    public static Logger logger;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {PacketTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return "com.matt.packetlistener.PacketCoreModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscated = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
