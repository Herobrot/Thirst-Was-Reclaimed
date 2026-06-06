package cn.mlus.thirst.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.teamlapen.vampirism.util.Helper;
import cn.mlus.thirst.Thirst;
import cn.mlus.thirst.compat.supernatural.SupernaturalHelper;
import cn.mlus.thirst.foundation.common.capability.IThirst;
import cn.mlus.thirst.foundation.common.capability.ModAttachment;
import cn.mlus.thirst.foundation.config.ClientConfig;
import cn.mlus.thirst.foundation.gui.appleskin.HUDOverlayHandler;
import net.neoforged.fml.ModList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class ThirstBarRenderer
{
    public static IThirst PLAYER_THIRST = null;
    public static ResourceLocation THIRST_ICONS = Thirst.asResource("textures/gui/thirst_icons.png");
    public static Boolean cancelRender = false;
    public static Boolean checkIfPlayerIsVampire = false;
    private static int lastBarRight;
    private static int lastBarTop;
    private static boolean renderedThisFrame;
    static Minecraft minecraft = Minecraft.getInstance();
    protected final static RandomSource random = RandomSource.create();

    @SubscribeEvent
    public static void onBeginRenderAir(RenderGuiEvent.Pre event)
    {
        if (event.getType() != RenderGuiEvent.Type.AIR)
            return;

        renderedThisFrame = false;
        cancelRender = !shouldRender(minecraft);
        if (cancelRender)
            return;

        setupOverlayRenderState(true, false);

        render(event.getScreenWidth(),event.getScreenHeight(),event.getGuiGraphics());
    }

    public static boolean shouldRender(Minecraft minecraft)
    {
        if (minecraft.player == null)
            return false;

        Entity vehicle = minecraft.player.getVehicle();
        boolean isMounted = vehicle != null && vehicle.showVehicleHealth();
        if (isMounted || minecraft.options.hideGui || !HUDOverlayHandler.shouldDrawSurvivalElements(minecraft))
            return false;

        if(checkIfPlayerIsVampire && Helper.isVampire(minecraft.player))
            return false;

        return minecraft.player.isAlive() && minecraft.player.getData(ModAttachment.PLAYER_THIRST).getShouldTickThirst();
    }

    public static int getBarRight(int width)
    {
        return renderedThisFrame ? lastBarRight : width / 2 + 91 + ClientConfig.THIRST_BAR_X_OFFSET.get();
    }

    public static int getBarTop(int height)
    {
        return renderedThisFrame ? lastBarTop : height - minecraft.gui.rightHeight + ClientConfig.THIRST_BAR_Y_OFFSET.get();
    }

    public static void setupOverlayRenderState(boolean blend, boolean depthTest)
    {
        if (blend)
        {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }
        else RenderSystem.disableBlend();

        if (depthTest)
            RenderSystem.enableDepthTest();
        else
            RenderSystem.disableDepthTest();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }

    public static void render(int width, int height, GuiGraphics guiGraphics)
    {
        minecraft.getProfiler().push("thirst");
        if (PLAYER_THIRST == null || minecraft.player.tickCount % 40 == 0)
        {
            PLAYER_THIRST = minecraft.player.getData(ModAttachment.PLAYER_THIRST);
        }

        ResourceLocation thirst_icons = THIRST_ICONS;
        if (ModList.get().isLoaded("supernatural")) {
            thirst_icons = SupernaturalHelper.getVampireIcons(thirst_icons, minecraft.player);
        }

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, thirst_icons);
        int left = width / 2 + 91 + ClientConfig.THIRST_BAR_X_OFFSET.get();
        int top = height - minecraft.gui.rightHeight + ClientConfig.THIRST_BAR_Y_OFFSET.get();
        minecraft.gui.rightHeight += 10;
        lastBarRight = left;
        lastBarTop = top;
        renderedThisFrame = true;

        int level = PLAYER_THIRST.getThirst();

        for (int i = 0; i < 10; ++i)
        {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;

            if (PLAYER_THIRST.getQuenched() <= 0.0F && minecraft.gui.getGuiTicks() % (level * 3 + 1) == 0)
            {
                y = top + (random.nextInt(3) - 1);
            }

            guiGraphics.blit(thirst_icons, x, y, 0, 0, 9, 9, 25, 9);

            if (idx < level)
                guiGraphics.blit(thirst_icons, x, y, 16, 0, 9, 9, 25, 9);
            else if (idx == level)
                guiGraphics.blit(thirst_icons, x, y, 8, 0, 9, 9, 25, 9);
        }
        RenderSystem.disableBlend();

        minecraft.getProfiler().pop();
    }
}
