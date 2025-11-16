package com.lunazstudios.inspectability.mixin.client;


import com.lunazstudios.inspectability.client.util.HeldItemTransformManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Shadow
    @Final
    private RenderItem itemRenderer;

    @Inject(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At("HEAD")
    )
    private void captureInitialTransformations(
            AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo ci
    ) {
        if (!HeldItemTransformManager.isInspectorMode()) {
            HeldItemTransformManager.setInitialTransformations(hand);
        }
    }

    @Inject(
            method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modifyHeldItemRendering(
            AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo ci
    ) {
        if (HeldItemTransformManager.isInspectorMode()) {
            ci.cancel();

            HeldItemTransformManager.updateTransition();
            GlStateManager.pushMatrix();

            GlStateManager.translate(
                    HeldItemTransformManager.getOffsetX(),
                    HeldItemTransformManager.getOffsetY(),
                    HeldItemTransformManager.getOffsetZ()
            );

            float scale = HeldItemTransformManager.getScale();
            GlStateManager.scale(scale, scale, scale);

            GlStateManager.rotate(HeldItemTransformManager.getRotationY(), 0, 1, 0);
            GlStateManager.rotate(HeldItemTransformManager.getRotationX(), 1, 0, 0);
            GlStateManager.rotate(HeldItemTransformManager.getRotationZ(), 0, 0, 1);

            this.itemRenderer.renderItem(
                    stack,
                    ItemCameraTransforms.TransformType.GUI
            );

            GlStateManager.popMatrix();
        }
    }
}
