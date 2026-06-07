package dev.ghen.thirst.foundation.network.message;

import dev.ghen.thirst.api.ThirstHelper;
import dev.ghen.thirst.foundation.network.ThirstModPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class ItemSettingsHashMessage
{
    public String settingsHash;

    public ItemSettingsHashMessage(String settingsHash)
    {
        this.settingsHash = settingsHash;
    }

    public static void encode(ItemSettingsHashMessage message, FriendlyByteBuf buffer)
    {
        byte[] bytes = message.settingsHash.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static ItemSettingsHashMessage decode(FriendlyByteBuf buffer)
    {
        int length = buffer.readInt();
        if (length < 0 || length > 1024)
            throw new IllegalArgumentException("Invalid settings hash length: " + length);

        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new ItemSettingsHashMessage(new String(bytes, StandardCharsets.UTF_8));
    }

    public static void handle(ItemSettingsHashMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient())
        {
            context.enqueueWork(() ->
            {
                if (!ThirstHelper.hasSyncedSettingsHash(message.settingsHash))
                    ThirstModPacketHandler.INSTANCE.sendToServer(new ItemSettingsSyncRequestMessage());
            });
        }

        context.setPacketHandled(true);
    }
}
