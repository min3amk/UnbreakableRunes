package com.amine.unbreakablerunes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

/** Saves, per dimension, which block positions are protected runes. */
public class RuneProtectionData extends SavedData {
    private static final String NAME = "unbreakablerunes_protected";
    private final Set<Long> positions = new HashSet<>();

    public static RuneProtectionData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(RuneProtectionData::new, RuneProtectionData::load, null), NAME);
    }

    public static RuneProtectionData load(CompoundTag tag, HolderLookup.Provider registries) {
        RuneProtectionData data = new RuneProtectionData();
        for (long l : tag.getLongArray("positions")) {
            data.positions.add(l);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putLongArray("positions", positions.stream().mapToLong(Long::longValue).toArray());
        return tag;
    }

    public boolean isProtected(BlockPos pos) {
        return positions.contains(pos.asLong());
    }

    public void protect(BlockPos pos) {
        if (positions.add(pos.asLong())) {
            setDirty();
        }
    }

    public void unprotect(BlockPos pos) {
        if (positions.remove(pos.asLong())) {
            setDirty();
        }
    }

    public boolean isEmpty() {
        return positions.isEmpty();
    }
}
