package cn.mlus.thirst.content.registry;

import com.mojang.serialization.MapCodec;
import cn.mlus.thirst.Thirst;
import cn.mlus.thirst.foundation.config.LootConfigCondition;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ConditionInit {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, Thirst.ID);

    public static final Supplier<MapCodec<LootConfigCondition>> LOOT_CONFIG_CONDITION = CONDITION_CODECS.register("loot_config", () -> LootConfigCondition.CODEC);
}
