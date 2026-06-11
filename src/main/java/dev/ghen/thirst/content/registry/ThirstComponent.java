package dev.ghen.thirst.content.registry;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @deprecated Compatibility bridge for addons compiled against the old package.
 * Use {@link cn.mlus.thirst.content.registry.ThirstComponent} instead.
 */
@Deprecated(forRemoval = false)
@SuppressWarnings("unused")
public class ThirstComponent
{
    public static final DeferredRegister<DataComponentType<?>> DR = cn.mlus.thirst.content.registry.ThirstComponent.DR;
    public static final DataComponentType<Integer> PURITY = cn.mlus.thirst.content.registry.ThirstComponent.PURITY;

    private ThirstComponent()
    {
    }
}
