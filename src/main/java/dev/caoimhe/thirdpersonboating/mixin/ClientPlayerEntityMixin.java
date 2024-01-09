package dev.caoimhe.thirdpersonboating.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends Entity {
    @Unique
    private @Nullable Perspective thirdPersonBoating$previousPerspective = null;

    private ClientPlayerEntityMixin() {
        // Required for the mixin to extend `Entity`.
        // We need to extend `Entity` in order to get access to `getVehicle()`.
        super(EntityType.PLAYER, null);
    }

    @Inject(at = @At("HEAD"), method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z")
    private void thirdPersonBoating$setPerspective(Entity entity, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof BoatEntity)) {
            return;
        }

        thirdPersonBoating$previousPerspective = MinecraftClient.getInstance().options.getPerspective();
        MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    @Inject(at = @At("HEAD"), method = "dismountVehicle")
    private void thirdPersonBoating$resetPerspective(CallbackInfo ci) {
        if (!(this.getVehicle() instanceof BoatEntity)) {
            return;
        }

        // This can occur if the player mounts an entity, disconnects, and then reconnects and dismounts.
        if (thirdPersonBoating$previousPerspective != null) {
            MinecraftClient.getInstance().options.setPerspective(thirdPersonBoating$previousPerspective);
        }
    }
}