package com.amine.unbreakablerunes;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

/**
 * Proof-of-concept protection for Ars Nouveau's Rune entity.
 *
 * This intentionally protects only the entity type ars_nouveau:rune. It does
 * not make the supporting floor block invulnerable and does not alter the
 * Rune's tick/collision logic, so entities should still be able to touch it.
 */
@Mod(UnbreakableRunes.MOD_ID)
public final class UnbreakableRunes {
    public static final String MOD_ID = "unbreakablerunes";
    private static final ResourceLocation ARS_RUNE_ID =
            ResourceLocation.fromNamespaceAndPath("ars_nouveau", "rune");

    public UnbreakableRunes() {
        // Event subscribers are discovered through the @Mod event bus bridge
        // on NeoForge's global game event bus.
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (ARS_RUNE_ID.equals(id)) {
            // Damage immunity only: keep normal ticking and collision behavior.
            entity.setInvulnerable(true);
        }
    }
}
