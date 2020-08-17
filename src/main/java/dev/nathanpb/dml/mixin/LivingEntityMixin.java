package dev.nathanpb.dml.mixin;

/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Refabricated.
 *
 * Deep Mob Learning: Refabricated is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 * Deep Mob Learning: Refabricated is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Refabricated.  If not, see <https://www.gnu.org/licenses/>.
 */

import dev.nathanpb.dml.accessor.ILivingEntityReiStateAccessor;
import dev.nathanpb.dml.entity.SystemGlitchEntity;
import dev.nathanpb.dml.entity.effect.StatusEffectsKt;
import dev.nathanpb.dml.event.LivingEntityDieCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements ILivingEntityReiStateAccessor  {
    boolean dmlRefIsInREIScreen = false;

    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntityDieCallback.EVENT.invoker().onDeath((LivingEntity) (Object) this, source);
    }

    @Inject(at = @At("HEAD"), method = "getMaxHealth", cancellable = true)
    public void getMaxHealth(CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof SystemGlitchEntity) {
            SystemGlitchEntity dis = (SystemGlitchEntity) ((Object)this);
            if (dis.getTier() != null) {
                float health = dis.getTier().getSystemGlitchMaxHealth();
                cir.setReturnValue(health);
                cir.cancel();
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"), method = "onEquipStack", cancellable = true)
    public void onEquip(ItemStack stack, CallbackInfo ci) {
        if (isDmlRefIsInReiScreen()) {
            ci.cancel();
        }
    }

    @Override
    public boolean isDmlRefIsInReiScreen() {
        return dmlRefIsInREIScreen;
    }

    @Override
    public void setDmlRefInReiScreen(boolean flag) {
        dmlRefIsInREIScreen = flag;
    }

    @ModifyVariable(
            at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;onGround:Z"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getBaseMovementSpeedMultiplier()F", ordinal = 0),
                    to = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;onGround:Z", ordinal = 0)
            ),
            method = "travel",
            ordinal = 2
    )
    public float depthStriderEffectTravelPath(float value) {
        LivingEntity dis = (LivingEntity) (Object) this;
        if (dis.hasStatusEffect(StatusEffectsKt.getDEPTH_STRIDER_EFFECT())) {
            return value + dis.getStatusEffect(StatusEffectsKt.getDEPTH_STRIDER_EFFECT()).getAmplifier();
        }
        return value;
    }
}
