package com.lunazstudios.inspectability;

import com.lunazstudios.inspectability.client.InspectorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class InspectabilityClient extends InspectabilityCommon {
    private static KeyBinding inspectorKey;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        inspectorKey = new KeyBinding(
                "key." + Tags.MOD_ID + ".openiteminspector",
                Keyboard.KEY_GRAVE,
                "category." + Tags.MOD_ID
        );
        ClientRegistry.registerKeyBinding(inspectorKey);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        while (inspectorKey.isPressed()) {
            Minecraft client = Minecraft.getMinecraft();
            EntityPlayerSP player = client.player;
            if (player != null) {
                client.displayGuiScreen(new InspectorScreen());
            }
        }
    }
}
