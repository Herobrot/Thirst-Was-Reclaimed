package cn.mlus.thirst;

import cn.mlus.thirst.api.ThirstHelper;
import cn.mlus.thirst.compat.create.CreateRegistry;
import cn.mlus.thirst.compat.create.ponder.ThirstPonders;
import cn.mlus.thirst.content.purity.WaterPurity;
import cn.mlus.thirst.content.registry.EffectInit;
import cn.mlus.thirst.content.registry.ItemInit;
import cn.mlus.thirst.content.thirst.PlayerThirst;
import cn.mlus.thirst.foundation.common.capability.IThirst;
import cn.mlus.thirst.foundation.common.loot.ModLootModifiers;
import cn.mlus.thirst.foundation.config.*;
import cn.mlus.thirst.foundation.gui.ThirstBarRenderer;
import cn.mlus.thirst.foundation.gui.appleskin.HUDOverlayHandler;
import cn.mlus.thirst.foundation.gui.appleskin.TooltipOverlayHandler;
import cn.mlus.thirst.foundation.network.ThirstModPacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Thirst.ID)
public class Thirst
{
    public static final String ID = "thirst";

    public Thirst()
    {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(this::commonSetup);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::registerCapabilities);

        if(FMLEnvironment.dist.isClient()){
            modBus.addListener(ThirstBarRenderer::registerThirstOverlay);

            if(ModList.get().isLoaded("appleskin"))
            {
                HUDOverlayHandler.init();
                TooltipOverlayHandler.init();
                modBus.addListener(this::onRegisterClientTooltipComponentFactories);
            }
        }


        ItemInit.ITEMS.register(modBus);
        EffectInit.MOB_EFFECTS.register(modBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modBus);

        if(ModList.get().isLoaded("create"))
        {
            CreateRegistry.register();
        }

        //configs
        ItemSettingsConfig.setup();
        CommonConfig.setup();
        ClientConfig.setup();
        KeyWordConfig.setup();
        ContainerConfig.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        WaterPurity.init();
        ThirstModPacketHandler.init();

        if(ModList.get().isLoaded("coldsweat"))
            ThirstHelper.shouldUseColdSweatCaps(true);

        if(ModList.get().isLoaded("tombstone"))
            PlayerThirst.checkTombstoneEffects = true;

        if(ModList.get().isLoaded("vampirism"))
            PlayerThirst.checkVampirismEffects = true;

        if(ModList.get().isLoaded("farmersdelight"))
            PlayerThirst.checkFDEffects = true;

        if(ModList.get().isLoaded("bakery"))
            PlayerThirst.checkLetsDoBakeryEffects = true;
    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        if(ModList.get().isLoaded("create")){
            event.enqueueWork(ThirstPonders::register);
        }

        if(ModList.get().isLoaded("vampirism"))
        {
            ThirstBarRenderer.checkIfPlayerIsVampire = true;
        }
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(IThirst.class);
    }

    //this is from Create but it looked very cool
    public static ResourceLocation asResource(String path)
    {
        return new ResourceLocation(ID, path);
    }
    private void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        TooltipOverlayHandler.register(event);
    }
}
