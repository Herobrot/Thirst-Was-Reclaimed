package dev.ghen.thirst.content.purity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * @deprecated Compatibility bridge for addons compiled against the old package.
 * Use {@link cn.mlus.thirst.content.purity.WaterPurity} instead.
 */
@Deprecated(forRemoval = false)
@SuppressWarnings({"unused", "deprecation"})
public class WaterPurity extends cn.mlus.thirst.content.purity.WaterPurity
{
    public static void init()
    {
        cn.mlus.thirst.content.purity.WaterPurity.init();
    }

    public static void addContainer(ContainerWithPurity container)
    {
        cn.mlus.thirst.content.purity.WaterPurity.addContainer(container);
    }

    public static void addContainer(cn.mlus.thirst.content.purity.ContainerWithPurity container)
    {
        cn.mlus.thirst.content.purity.WaterPurity.addContainer(container);
    }

    public static ItemStack getFilledContainer(ItemStack container, boolean fromFilled)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.getFilledContainer(container, fromFilled);
    }

    public static boolean isWaterFilledContainer(ItemStack item)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.isWaterFilledContainer(item);
    }

    public static boolean isEmptyWaterContainer(ItemStack item)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.isEmptyWaterContainer(item);
    }

    public static Integer getPurity(ItemStack item)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.getPurity(item);
    }

    public static Integer getPurity(FluidStack fluid)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.getPurity(fluid);
    }

    public static void tanPurity(ItemStack item)
    {
        cn.mlus.thirst.content.purity.WaterPurity.tanPurity(item);
    }

    public static int getBlockPurity(BlockState blockState)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.getBlockPurity(blockState);
    }

    public static int getBlockPurity(Level level, BlockPos pos)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.getBlockPurity(level, pos);
    }

    public static boolean hasPurity(ItemStack item)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.hasPurity(item);
    }

    public static boolean hasPurity(FluidStack fluid)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.hasPurity(fluid);
    }

    public static ItemStack addPurity(ItemStack item, BlockPos pos, Level level)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.addPurity(item, pos, level);
    }

    public static ItemStack addPurity(ItemStack item, int purity)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.addPurity(item, purity);
    }

    public static FluidStack addPurity(FluidStack fluid, int purity)
    {
        return cn.mlus.thirst.content.purity.WaterPurity.addPurity(fluid, purity);
    }
}
