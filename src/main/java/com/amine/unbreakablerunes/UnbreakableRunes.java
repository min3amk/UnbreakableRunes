package com.amine.unbreakablerunes;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(UnbreakableRunes.MOD_ID)
public final class UnbreakableRunes {
    public static final String MOD_ID = "unbreakablerunes";

    public UnbreakableRunes(IEventBus modBus) {
        // Registers the new glyph so it shows up in the spell book.
        GlyphRegistry.registerSpell(EffectUnbreakable.INSTANCE);
    }
}
