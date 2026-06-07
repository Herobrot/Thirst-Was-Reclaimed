package cn.mlus.thirst.foundation.network.message;

import cn.mlus.thirst.foundation.network.ThirstModPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemSettingsSyncRequestMessage
{
    public ItemSettingsSyncRequestMessage() {}

    public static void encode(ItemSettingsSyncRequestMessage message, FriendlyByteBuf buffer)
    {
    }

    public static ItemSettingsSyncRequestMessage decode(FriendlyByteBuf buffer)
    {
        return new ItemSettingsSyncRequestMessage();
    }

    public static void handle(ItemSettingsSyncRequestMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isServer())
        {
            context.enqueueWork(() ->
            {
                ServerPlayer player = context.getSender();
                if (player != null)
                    ThirstModPacketHandler.INSTANCE.send(
                            net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
                            ItemSettingsSyncMessage.create()
                    );
            });
        }

        context.setPacketHandled(true);
    }
}
