package com.lunazstudios.inspectability;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,
        clientSideOnly = true, acceptableRemoteVersions = "*", dependencies = "required-after:mixinbooter@[8.0,)")
public class Inspectability {

    @SidedProxy(
            clientSide = "com.lunazstudios.inspectability.InspectabilityClient",
            serverSide = "com.lunazstudios.inspectability.InspectabilityCommon"
    )
    public static InspectabilityCommon proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }
}
