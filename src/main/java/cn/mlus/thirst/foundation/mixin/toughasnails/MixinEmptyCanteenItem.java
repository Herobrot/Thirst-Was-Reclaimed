package cn.mlus.thirst.foundation.mixin.toughasnails;

import cn.mlus.thirst.content.purity.WaterPurity;
import cn.mlus.thirst.foundation.util.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toughasnails.api.block.TANBlocks;
import toughasnails.block.RainCollectorBlock;
import toughasnails.item.EmptyCanteenItem;

@Mixin(EmptyCanteenItem.class)
public abstract class MixinEmptyCanteenItem {

    @Shadow(remap = false)
    protected abstract ItemStack replaceCanteen(ItemStack stack, Player player, ItemStack filledItem);

    @Shadow public abstract Item getPurifiedWaterCanteen();

    @Shadow public abstract Item getWaterCanteen();

    @Shadow public abstract Item getDirtyWaterCanteen();

    @Inject(method = "fillCanteen", at = @At("HEAD"), cancellable = true, remap = false)
    private void fillCanteen(Level world, Player player, ItemStack stack, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir){
        if(!WaterPurity.isEnabled())
            return;

        Level level = player.level();

        BlockPos blockPos = MathHelper.getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY).getBlockPos();
        BlockState state = world.getBlockState(blockPos);

        if (!world.mayInteract(player, blockPos)){
            cir.setReturnValue(InteractionResultHolder.pass(stack));
            return;
        }

        if(level.getFluidState(blockPos).is(FluidTags.WATER))
        {
            SoundEvent sound=SoundEvents.BOTTLE_FILL;
            ItemStack filledItem;

            level.playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);

            int purity=WaterPurity.getBlockPurity(level, blockPos);
            if(purity==3){
                filledItem = getPurifiedWaterCanteen().getDefaultInstance();
            } else if (purity==2) {
                filledItem = getWaterCanteen().getDefaultInstance();
            }else {
                filledItem = getDirtyWaterCanteen().getDefaultInstance();
            }

            cir.setReturnValue(InteractionResultHolder.sidedSuccess(replaceCanteen(stack, player, filledItem), world.isClientSide()));
        }
        else if (state.getBlock() instanceof RainCollectorBlock) {
            int waterLevel = state.getValue(RainCollectorBlock.LEVEL);
            if (waterLevel > 0 && !world.isClientSide()) {
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                ((RainCollectorBlock) TANBlocks.RAIN_COLLECTOR).setWaterLevel(world, blockPos, state, waterLevel - 1);
                cir.setReturnValue(InteractionResultHolder.success(replaceCanteen(stack, player, getPurifiedWaterCanteen().getDefaultInstance())));
            }
        }
    }
}
