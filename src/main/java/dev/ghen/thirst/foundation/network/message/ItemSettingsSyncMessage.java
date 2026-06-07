package dev.ghen.thirst.foundation.network.message;

import dev.ghen.thirst.api.ThirstHelper;
import dev.ghen.thirst.content.purity.WaterPurity;
import dev.ghen.thirst.foundation.config.CommonConfig;
import dev.ghen.thirst.foundation.config.ContainerConfig;
import dev.ghen.thirst.foundation.config.ItemSettingsConfig;
import dev.ghen.thirst.foundation.config.KeyWordConfig;
import dev.ghen.thirst.foundation.util.ConfigHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Supplier;

public class ItemSettingsSyncMessage
{
    public final List<Entry> drinks;
    public final List<Entry> foods;
    public final List<String> blacklist;
    public final List<String> containers;
    public final boolean keywordEnabled;
    public final String keywordBlacklist;
    public final String keywordDrink;
    public final String keywordSoup;
    public final String keywordFruit;
    public final int defaultDrinkHydration;
    public final int defaultDrinkQuenched;
    public final int defaultSoupHydration;
    public final int defaultSoupQuenched;
    public final int defaultFruitHydration;
    public final int defaultFruitQuenched;
    public final boolean purityEnabled;

    public ItemSettingsSyncMessage(
            List<Entry> drinks,
            List<Entry> foods,
            List<String> blacklist,
            List<String> containers,
            boolean keywordEnabled,
            String keywordBlacklist,
            String keywordDrink,
            String keywordSoup,
            String keywordFruit,
            int defaultDrinkHydration,
            int defaultDrinkQuenched,
            int defaultSoupHydration,
            int defaultSoupQuenched,
            int defaultFruitHydration,
            int defaultFruitQuenched,
            boolean purityEnabled
    )
    {
        this.drinks = drinks;
        this.foods = foods;
        this.blacklist = blacklist;
        this.containers = containers;
        this.keywordEnabled = keywordEnabled;
        this.keywordBlacklist = keywordBlacklist;
        this.keywordDrink = keywordDrink;
        this.keywordSoup = keywordSoup;
        this.keywordFruit = keywordFruit;
        this.defaultDrinkHydration = defaultDrinkHydration;
        this.defaultDrinkQuenched = defaultDrinkQuenched;
        this.defaultSoupHydration = defaultSoupHydration;
        this.defaultSoupQuenched = defaultSoupQuenched;
        this.defaultFruitHydration = defaultFruitHydration;
        this.defaultFruitQuenched = defaultFruitQuenched;
        this.purityEnabled = purityEnabled;
    }

    public static class Entry
    {
        public final String itemId;
        public final int thirst;
        public final int quenched;

        public Entry(String itemId, int thirst, int quenched)
        {
            this.itemId = itemId;
            this.thirst = thirst;
            this.quenched = quenched;
        }
    }

    public static void encode(ItemSettingsSyncMessage message, FriendlyByteBuf buffer)
    {
        writeEntries(buffer, message.drinks);
        writeEntries(buffer, message.foods);
        writeStrings(buffer, message.blacklist);
        writeStrings(buffer, message.containers);
        buffer.writeBoolean(message.keywordEnabled);
        writeString(buffer, message.keywordBlacklist);
        writeString(buffer, message.keywordDrink);
        writeString(buffer, message.keywordSoup);
        writeString(buffer, message.keywordFruit);
        buffer.writeInt(message.defaultDrinkHydration);
        buffer.writeInt(message.defaultDrinkQuenched);
        buffer.writeInt(message.defaultSoupHydration);
        buffer.writeInt(message.defaultSoupQuenched);
        buffer.writeInt(message.defaultFruitHydration);
        buffer.writeInt(message.defaultFruitQuenched);
        buffer.writeBoolean(message.purityEnabled);
    }

    public static ItemSettingsSyncMessage decode(FriendlyByteBuf buffer)
    {
        List<Entry> drinks = readEntries(buffer);
        List<Entry> foods = readEntries(buffer);
        List<String> blacklist = readStrings(buffer);
        List<String> containers = readStrings(buffer);
        boolean keywordEnabled = buffer.readBoolean();
        String keywordBlacklist = readString(buffer);
        String keywordDrink = readString(buffer);
        String keywordSoup = readString(buffer);
        String keywordFruit = readString(buffer);
        int defaultDrinkHydration = buffer.readInt();
        int defaultDrinkQuenched = buffer.readInt();
        int defaultSoupHydration = buffer.readInt();
        int defaultSoupQuenched = buffer.readInt();
        int defaultFruitHydration = buffer.readInt();
        int defaultFruitQuenched = buffer.readInt();
        boolean purityEnabled = buffer.readBoolean();
        return new ItemSettingsSyncMessage(
                drinks, foods, blacklist, containers,
                keywordEnabled, keywordBlacklist, keywordDrink, keywordSoup, keywordFruit,
                defaultDrinkHydration, defaultDrinkQuenched,
                defaultSoupHydration, defaultSoupQuenched,
                defaultFruitHydration, defaultFruitQuenched,
                purityEnabled
        );
    }

    public static void handle(ItemSettingsSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient())
        {
            context.enqueueWork(() ->
            {
                List<Item> syncedContainers = toItems(message.containers);
                ThirstHelper.applySyncedItemSettings(
                        toMap(message.drinks),
                        toMap(message.foods),
                        message.blacklist,
                        message.keywordEnabled,
                        message.keywordBlacklist,
                        message.keywordDrink,
                        message.keywordSoup,
                        message.keywordFruit,
                        message.defaultDrinkHydration,
                        message.defaultDrinkQuenched,
                        message.defaultSoupHydration,
                        message.defaultSoupQuenched,
                        message.defaultFruitHydration,
                        message.defaultFruitQuenched
                );
                ThirstHelper.applySyncedContainers(syncedContainers);
                ThirstHelper.applySyncedSettingsHash(message.contentHash());
                WaterPurity.applySyncedEnabled(message.purityEnabled);
                WaterPurity.applySyncedContainers(syncedContainers);
            });
        }

        context.setPacketHandled(true);
    }

    public static ItemSettingsSyncMessage create()
    {
        return new ItemSettingsSyncMessage(
                toEntries(ThirstHelper.VALID_DRINKS),
                toEntries(ThirstHelper.VALID_FOODS),
                new ArrayList<>(ItemSettingsConfig.ITEMS_BLACKLIST.get()),
                toItemIds(ThirstHelper.containers),
                KeyWordConfig.ENABLE_KEYWORD_CONFIG.get(),
                KeyWordConfig.KEYWORD_BLACKLIST.get(),
                KeyWordConfig.KEYWORD_DRINK.get(),
                KeyWordConfig.KEYWORD_SOUP.get(),
                KeyWordConfig.KEYWORD_FRUIT.get(),
                KeyWordConfig.getDrinkHydration(),
                KeyWordConfig.getDrinkQuenchness(),
                KeyWordConfig.getSoupHydration(),
                KeyWordConfig.getSoupQuenchness(),
                KeyWordConfig.getFruitHydration(),
                KeyWordConfig.getFruitQuenchness(),
                CommonConfig.ENABLE_PURITY.get()
        );
    }

    private static List<Entry> toEntries(Map<Item, Number[]> source)
    {
        List<Entry> entries = new ArrayList<>();
        source.forEach((item, values) ->
        {
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if (id != null)
                entries.add(new Entry(id.toString(), values[0].intValue(), values[1].intValue()));
        });
        entries.sort(Comparator.comparing(e -> e.itemId));
        return entries;
    }

    private static List<String> toItemIds(List<Item> items)
    {
        List<String> entries = new ArrayList<>();
        for (Item item : items)
        {
            if (item != Items.AIR)
            {
                ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
                if (id != null)
                    entries.add(id.toString());
            }
        }
        entries.sort(String::compareTo);
        return entries;
    }

    private static Map<Item, Number[]> toMap(List<Entry> source)
    {
        Map<Item, Number[]> map = new HashMap<>();
        for (Entry entry : source)
        {
            ResourceLocation id = tryParseResourceLocation(entry.itemId);
            if (id == null)
                continue;

            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != null)
                map.put(item, new Number[]{entry.thirst, entry.quenched});
        }
        return map;
    }

    private static List<Item> toItems(List<String> source)
    {
        List<Item> items = new ArrayList<>();
        for (String itemId : source)
        {
            ResourceLocation id = tryParseResourceLocation(itemId);
            if (id == null)
                continue;

            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item != Items.AIR && item != null)
                items.add(item);
        }
        return items;
    }

    private static ResourceLocation tryParseResourceLocation(String s)
    {
        try
        {
            return new ResourceLocation(s);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private static void writeEntries(FriendlyByteBuf buffer, List<Entry> entries)
    {
        buffer.writeInt(entries.size());
        for (Entry entry : entries)
        {
            writeString(buffer, entry.itemId);
            buffer.writeInt(entry.thirst);
            buffer.writeInt(entry.quenched);
        }
    }

    private static List<Entry> readEntries(FriendlyByteBuf buffer)
    {
        int size = buffer.readInt();
        List<Entry> entries = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
        {
            entries.add(new Entry(readString(buffer), buffer.readInt(), buffer.readInt()));
        }
        return entries;
    }

    private static void writeStrings(FriendlyByteBuf buffer, List<String> strings)
    {
        buffer.writeInt(strings.size());
        for (String string : strings)
        {
            writeString(buffer, string);
        }
    }

    private static List<String> readStrings(FriendlyByteBuf buffer)
    {
        int size = buffer.readInt();
        List<String> strings = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
        {
            strings.add(readString(buffer));
        }
        return strings;
    }

    private static void writeString(FriendlyByteBuf buffer, String string)
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    private static String readString(FriendlyByteBuf buffer)
    {
        int length = buffer.readInt();
        if (length < 0 || length > 1_048_576)
            throw new IllegalArgumentException("Invalid synced string length: " + length);

        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String createHash()
    {
        return create().contentHash();
    }

    public String contentHash()
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            updateEntries(digest, drinks);
            updateEntries(digest, foods);
            updateStrings(digest, blacklist);
            updateStrings(digest, containers);
            updateBoolean(digest, keywordEnabled);
            updateString(digest, keywordBlacklist);
            updateString(digest, keywordDrink);
            updateString(digest, keywordSoup);
            updateString(digest, keywordFruit);
            updateInt(digest, defaultDrinkHydration);
            updateInt(digest, defaultDrinkQuenched);
            updateInt(digest, defaultSoupHydration);
            updateInt(digest, defaultSoupQuenched);
            updateInt(digest, defaultFruitHydration);
            updateInt(digest, defaultFruitQuenched);
            updateBoolean(digest, purityEnabled);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest.digest())
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException exception)
        {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    private static void updateEntries(MessageDigest digest, List<Entry> entries)
    {
        updateInt(digest, entries.size());
        for (Entry entry : entries)
        {
            updateString(digest, entry.itemId);
            updateInt(digest, entry.thirst);
            updateInt(digest, entry.quenched);
        }
    }

    private static void updateStrings(MessageDigest digest, List<String> strings)
    {
        updateInt(digest, strings.size());
        for (String string : strings)
        {
            updateString(digest, string);
        }
    }

    private static void updateString(MessageDigest digest, String string)
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        updateInt(digest, bytes.length);
        digest.update(bytes);
    }

    private static void updateBoolean(MessageDigest digest, boolean value)
    {
        digest.update((byte) (value ? 1 : 0));
    }

    private static void updateInt(MessageDigest digest, int value)
    {
        digest.update((byte) (value >>> 24));
        digest.update((byte) (value >>> 16));
        digest.update((byte) (value >>> 8));
        digest.update((byte) value);
    }
}
