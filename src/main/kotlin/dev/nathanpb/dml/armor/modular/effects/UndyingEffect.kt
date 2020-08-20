/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Refabricated.
 *
 * Deep Mob Learning: Refabricated is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deep Mob Learning: Refabricated is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Refabricated.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.nathanpb.dml.armor.modular.effects

import dev.nathanpb.dml.armor.modular.core.EffectStackOption
import dev.nathanpb.dml.armor.modular.core.ModularEffect
import dev.nathanpb.dml.armor.modular.core.ModularEffectContext
import dev.nathanpb.dml.armor.modular.core.ModularEffectTriggerPayload
import dev.nathanpb.dml.config
import dev.nathanpb.dml.enums.DataModelTier
import dev.nathanpb.dml.enums.EntityCategory
import dev.nathanpb.dml.event.context.FindTotemOfUndyingCallback
import dev.nathanpb.dml.identifier
import dev.nathanpb.dml.utils.firstNonNullMapping
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ActionResult

class UndyingEffect : ModularEffect<ModularEffectTriggerPayload>(
    identifier("undying"),
    EntityCategory.ILLAGER,
    config.glitchArmor::enableUndying,
    config.glitchArmor::undyingCost
) {
    override fun registerEvents() {
        FindTotemOfUndyingCallback.register { player ->
            ModularEffectContext.from(player)
                .run(EffectStackOption.RANDOMIZE.apply)
                .firstNonNullMapping {
                    val result = attemptToApply(it, ModularEffectTriggerPayload.EMPTY)
                    if (result == ActionResult.SUCCESS) {
                        ItemStack(Items.TOTEM_OF_UNDYING)
                    } else null
                }
        }
    }

    override fun acceptTier(tier: DataModelTier) = tier.isMaxTier()
}
