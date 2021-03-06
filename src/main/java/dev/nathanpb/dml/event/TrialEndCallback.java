package dev.nathanpb.dml.event;

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

import dev.nathanpb.dml.trial.Trial;
import dev.nathanpb.dml.trial.TrialEndReason;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface TrialEndCallback {
    Event<TrialEndCallback> EVENT = EventFactory.createArrayBacked(TrialEndCallback.class, listeners ->
        (trial, reason) -> {
            for(TrialEndCallback listener : listeners) {
                listener.onTrialEnd(trial, reason);
            }
        }
    );

    void onTrialEnd(Trial trial, TrialEndReason reason);
}
