package net.michaeljackson23.mineademia.items.custom;

import net.michaeljackson23.mineademia.abilitysystem.impl.AbilityManager;
import net.michaeljackson23.mineademia.abilitysystem.intr.abilityyser.IPlayerAbilityUser;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EnergyDrinkItem extends Item {

    public EnergyDrinkItem() {
        super(new Settings().maxCount(16).food(new FoodComponent.Builder().alwaysEdible().build()));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity entity) {
        if (entity instanceof ServerPlayerEntity player) {
            IPlayerAbilityUser user = AbilityManager.getPlayerUser(player);
            if (user != null)
                user.setStamina(user.getMaxStamina());

            Criteria.CONSUME_ITEM.trigger(player, stack);
            entity.emitGameEvent(GameEvent.DRINK);
        }

        return super.finishUsing(stack, world, entity);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.mineademia.energy_drink.tooltip_1").formatted(Formatting.ITALIC, Formatting.BLUE));
        tooltip.add(Text.translatable("item.mineademia.energy_drink.tooltip_2").formatted(Formatting.GRAY));
    }

}
