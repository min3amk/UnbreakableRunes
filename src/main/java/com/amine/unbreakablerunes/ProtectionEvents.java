package com.amine.unbreakablerunes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

@EventBusSubscriber(modid = UnbreakableRunes.MOD_ID)
public class ProtectionEvents {

    /** Blocks breaking by players (and by other mods that fire a break event). */
    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        RuneProtectionData data = RuneProtectionData.get(level);
        BlockPos pos = event.getPos();
        if (!data.isProtected(pos)) {
            return;
        }
        // Escape hatch: creative players can still remove it (and it is unprotected).
        if (event.getPlayer() != null && event.getPlayer().isCreative()) {
            data.unprotect(pos);
            return;
        }
        event.setCanceled(true);
    }

    /** Keeps protected runes out of explosions. */
    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        RuneProtectionData data = RuneProtectionData.get(level);
        if (data.isEmpty()) {
            return;
        }
        event.getAffectedBlocks().removeIf(data::isProtected);
    }
}
