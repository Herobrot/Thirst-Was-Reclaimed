package cn.mlus.thirst.foundation.network.message;

import cn.mlus.thirst.Thirst;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ItemSettingsSyncRequestMessage() implements CustomPacketPayload
{
    public static final CustomPacketPayload.Type<ItemSettingsSyncRequestMessage> TYPE = new Type<>(Thirst.asResource("item_settings_sync_request"));

    public static final StreamCodec<ByteBuf, ItemSettingsSyncRequestMessage> STREAM_CODEC = new StreamCodec<>()
    {
        @Override
        public ItemSettingsSyncRequestMessage decode(ByteBuf buffer)
        {
            return new ItemSettingsSyncRequestMessage();
        }

        @Override
        public void encode(ByteBuf buffer, ItemSettingsSyncRequestMessage message)
        {

        }
    };

    public static void serverHandle(final ItemSettingsSyncRequestMessage message, final IPayloadContext context)
    {
        context.enqueueWork(() ->
        {
            if (context.player() instanceof ServerPlayer player)
                PacketDistributor.sendToPlayer(player, ItemSettingsSyncMessage.create());
        });
    }

    public static void clientHandle(final ItemSettingsSyncRequestMessage message, final IPayloadContext context)
    {

    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type()
    {
        return TYPE;
    }
}
