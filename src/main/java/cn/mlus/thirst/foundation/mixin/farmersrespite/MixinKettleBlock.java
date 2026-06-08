package cn.mlus.thirst.foundation.mixin.farmersrespite;

import cn.mlus.thirst.content.purity.WaterPurity;
import com.farmersrespite.common.block.KettleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KettleBlock.class)
public abstract class MixinKettleBlock {

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    protected void addPurityBlockState(StateDefinition.Builder<Block, BlockState> p_153549_, CallbackInfo ci) {
        p_153549_.add(WaterPurity.BLOCK_PURITY);
    }

}