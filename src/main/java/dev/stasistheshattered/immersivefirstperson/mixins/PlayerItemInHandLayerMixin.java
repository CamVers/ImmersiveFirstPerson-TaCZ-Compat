package dev.stasistheshattered.immersivefirstperson.mixins;

import com.tacz.guns.api.item.IGun;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * IFP renders the local player's complete body through PlayerRenderer while in
 * first person. The player's held-item layer then renders the gun with a
 * THIRD_PERSON_* transform, which is why the low-detail third-person TaCZ model
 * appears in the camera. Suppress only this body-layer gun draw. TaCZ's normal
 * first-person ItemInHandRenderer/RenderHandEvent pipeline remains untouched.
 */
@Mixin(PlayerItemInHandLayer.class)
public abstract class PlayerItemInHandLayerMixin {
    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    private void immersiveFirstPerson$hideThirdPersonGunInFirstPersonBody(
            LivingEntity entity,
            ItemStack stack,
            ItemDisplayContext displayContext,
            HumanoidArm arm,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci
    ) {
        Minecraft minecraft = Minecraft.getInstance();
        if (entity == minecraft.player
                && minecraft.options.getCameraType().isFirstPerson()
                && stack.getItem() instanceof IGun) {
            ci.cancel();
        }
    }
}
