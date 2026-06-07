package cn.mlus.thirst.foundation.mixin;

import cn.mlus.thirst.content.purity.WaterPurity;
import cn.mlus.thirst.foundation.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public class MixinBottleItem
{
    private static final ThreadLocal<Integer> BOTTLE_PURITY = new ThreadLocal<>();

    @Inject(method = "turnBottleIntoItem", at = @At("HEAD"))
    public void setPurity(ItemStack source, Player player, ItemStack result, CallbackInfoReturnable<ItemStack> cir)
    {
        if(!WaterPurity.isEnabled())
        {
            BOTTLE_PURITY.set(null);
            return;
        }

        Level level = player.level();
        BlockPos fluidPos = MathHelper.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY).getBlockPos();
        boolean shouldModify = level.getFluidState(fluidPos).is(FluidTags.WATER) && level.getFluidState(fluidPos).isSource();
        BOTTLE_PURITY.set(shouldModify ? WaterPurity.getBlockPurity(level, fluidPos) : null);
    }

    @ModifyArg(method = "turnBottleIntoItem", index = 2, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemUtils;createFilledResult(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"))
    private ItemStack addPurity(ItemStack result)
    {
        Integer purity = BOTTLE_PURITY.get();
        if (purity != null)
        {
            WaterPurity.addPurity(result, purity);
        }
        return result;
    }

    @Inject(method = "turnBottleIntoItem", at = @At("RETURN"))
    public void cleanup(ItemStack source, Player player, ItemStack result, CallbackInfoReturnable<ItemStack> cir)
    {
        BOTTLE_PURITY.remove();
    }
}
