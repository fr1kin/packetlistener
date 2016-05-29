package com.matt.packetlistener;

import com.google.common.eventbus.EventBus;
import com.matt.autotradermod.AutoTraderMod;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;

public class PacketCoreModContainer extends DummyModContainer {
    public PacketCoreModContainer() {
        super(new ModMetadata());

        ModMetadata metaData = super.getMetadata();
        metaData.authorList = Arrays.asList("fr1kin");
        metaData.description = "CoreMod for listening to packets.";
        metaData.modId = "packetlistenercore";
        metaData.version = "1.0";
        metaData.name = "Packet Listener Core";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        PacketCoreMod.logger = LogManager.getLogger(FMLCommonHandler.instance().findContainerFor(this));
        return true;
    }
}
