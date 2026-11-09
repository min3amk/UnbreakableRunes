package com.amine.unbreakablerunes;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.AbstractEffect;
import com.hollingsworth.arsnouveau.api.spell.SpellContext;
import com.hollingsworth.arsnouveau.api.spell.SpellResolver;
import com.hollingsworth.arsnouveau.api.spell.SpellSchool;
import com.hollingsworth.arsnouveau.api.spell.SpellSchools;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

/**
 * Glyph "Unbreakable": use it after Touch (or similar) aimed at an Ars Nouveau
 * rune. The rune's position is saved as protected. Casting it again removes
 * the protection.
 */
public class EffectUnbreakable extends AbstractEffect {
    public static final EffectUnbreakable INSTANCE = new EffectUnbreakable();

    public EffectUnbreakable() {
        super(ResourceLocation.fromNamespaceAndPath(UnbreakableRunes.MOD_ID, "glyph_unbreakable"), "Unbreakable");
    }

    @Override
    public void onResolveBlock(BlockHitResult rayTraceResult, Level world, @NotNull LivingEntity shooter,
                               SpellStats spellStats, SpellContext spellContext, SpellResolver resolver) {
        if (!(world instanceof ServerLevel level)) {
            return;
        }
        BlockPos pos = rayTraceResult.getBlockPos();
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(world.getBlockState(pos).getBlock());
        boolean isRune = id.getNamespace().equals("ars_nouveau") && id.getPath().contains("rune");
        if (!isRune) {
            return;
        }

        RuneProtectionData data = RuneProtectionData.get(level);
        boolean nowProtected;
        if (data.isProtected(pos)) {
            data.unprotect(pos);
            nowProtected = false;
        } else {
            data.protect(pos);
            nowProtected = true;
        }
        if (shooter instanceof Player player) {
            player.displayClientMessage(Component.literal(nowProtected
                    ? "Rune is now unbreakable"
                    : "Rune is breakable again"), true);
        }
    }

    // The methods below are written without @Override on purpose: if Ars
    // Nouveau's version uses a slightly different signature, the compiler
    // error will tell us exactly what to adjust.

    public int getDefaultManaCost() {
        return 50;
    }

    @NotNull
    public Set<AbstractAugment> getCompatibleAugments() {
        return Collections.emptySet();
    }

    public SpellTier defaultTier() {
        return SpellTier.ONE;
    }

    @NotNull
    public Set<SpellSchool> getSchools() {
        return Set.of(SpellSchools.MANIPULATION);
    }

    public String getBookDescription() {
        return "Makes the Ars Nouveau rune you touch unbreakable. Cast it again on the same rune to undo.";
    }
}
