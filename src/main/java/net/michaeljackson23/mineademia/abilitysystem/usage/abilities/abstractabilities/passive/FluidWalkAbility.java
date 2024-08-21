package net.michaeljackson23.mineademia.abilitysystem.usage.abilities.abstractabilities.passive;

import net.michaeljackson23.mineademia.abilitysystem.impl.ability.passive.DataPassiveAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.ability.IAbility;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IAbilityUser;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;

public class FluidWalkAbility extends DataPassiveAbility {

    public static final HashSet<Fluid> DEFAULT_FLUIDS = new HashSet<>() {{
        add(Fluids.WATER);
        add(Fluids.LAVA);
    }};


    private boolean fluidWalking;
    private boolean fallOnSneak;

    private final HashSet<Fluid> walkableFluids;

    public FluidWalkAbility(@NotNull IAbilityUser user) {
        super(user, "Water Walk", "");

        this.fluidWalking = false;
        this.fallOnSneak = true;

        this.walkableFluids = new HashSet<>();
        walkableFluids.addAll(DEFAULT_FLUIDS);
    }

    public boolean isFluidWalking() {
        return fluidWalking;
    }
    public void setFluidWalking(boolean fluidWalking) {
        this.fluidWalking = fluidWalking;
    }

    public boolean isFallOnSneak() {
        return fallOnSneak;
    }
    public void setFallOnSneak(boolean fallOnSneak) {
        this.fallOnSneak = fallOnSneak;
    }

    public boolean canWalkOn(@NotNull Fluid fluid) {
        LivingEntity entity = getEntity();

        // Fails if you're submerged in fluid
        BlockState footState = entity.getWorld().getBlockState(entity.getBlockPos());
        if (footState.getBlock() instanceof FluidBlock)
            return false;

        return fluidWalking && (!fallOnSneak || !entity.isSneaking()) && walkableFluids.contains(fluid);
    }

    public void setWalkOn(@NotNull Fluid... fluids) {
        walkableFluids.addAll(List.of(fluids));
    }
    public void removeWalkOn(@NotNull Fluid... fluids) {
        List.of(fluids).forEach(walkableFluids::remove);
    }


    public static boolean canWalkOn(@NotNull Entity entity, @NotNull Fluid fluid) {
        FluidWalkAbility fluidWalkAbility = IAbility.getAbility(entity, FluidWalkAbility.class);
        return fluidWalkAbility != null && fluidWalkAbility.canWalkOn(fluid);
    }

}
