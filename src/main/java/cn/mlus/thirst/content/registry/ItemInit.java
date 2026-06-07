package cn.mlus.thirst.content.registry;

import cn.mlus.thirst.foundation.common.item.DrinkableItem;
import cn.mlus.thirst.foundation.tab.ThirstTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> CLAY_BOWL;
    public static final RegistryObject<Item> TERRACOTTA_BOWL;
    public static final RegistryObject<Item> TERRACOTTA_WATER_BOWL;

    public ItemInit() {
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "thirst");
        CLAY_BOWL = ITEMS.register("clay_bowl", () -> new Item((new Item.Properties())
                .stacksTo(64)
                .tab(ThirstTab.THIRST_TAB)
        ));
        TERRACOTTA_BOWL = ITEMS.register("terracotta_bowl", () -> new Item((new Item.Properties())
                .stacksTo(64)
                .tab(ThirstTab.THIRST_TAB)
        ));
        TERRACOTTA_WATER_BOWL = ITEMS.register("terracotta_water_bowl", () -> (new DrinkableItem())
                .setContainer(TERRACOTTA_BOWL.get())
        );
    }
}