package cn.mlus.thirst.foundation.network;

import cn.mlus.thirst.foundation.network.message.DrinkByHandMessage;
import cn.mlus.thirst.foundation.network.message.ItemSettingsHashMessage;
import cn.mlus.thirst.foundation.network.message.ItemSettingsSyncMessage;
import cn.mlus.thirst.foundation.network.message.ItemSettingsSyncRequestMessage;
import cn.mlus.thirst.foundation.network.message.PlayerThirstSyncMessage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber
public class ThirstModPacketHandler
{
    private static final String PROTOCOL_VERSION = "0.1.5";

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playBidirectional(
                DrinkByHandMessage.TYPE,
                DrinkByHandMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        DrinkByHandMessage::clientHandle,
                        DrinkByHandMessage::serverHandle
                )
        );
        registrar.playBidirectional(
                PlayerThirstSyncMessage.TYPE,
                PlayerThirstSyncMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        PlayerThirstSyncMessage::clientHandle,
                        PlayerThirstSyncMessage::serverHandle
                )
        );
        registrar.playBidirectional(
                ItemSettingsHashMessage.TYPE,
                ItemSettingsHashMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ItemSettingsHashMessage::clientHandle,
                        ItemSettingsHashMessage::serverHandle
                )
        );
        registrar.playBidirectional(
                ItemSettingsSyncMessage.TYPE,
                ItemSettingsSyncMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ItemSettingsSyncMessage::clientHandle,
                        ItemSettingsSyncMessage::serverHandle
                )
        );
        registrar.playBidirectional(
                ItemSettingsSyncRequestMessage.TYPE,
                ItemSettingsSyncRequestMessage.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ItemSettingsSyncRequestMessage::clientHandle,
                        ItemSettingsSyncRequestMessage::serverHandle
                )
        );
    }
}
