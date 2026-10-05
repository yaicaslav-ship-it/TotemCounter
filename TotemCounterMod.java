package com.example.totemcounter;

import com.example.totemcounter.network.TotemSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TotemCounterMod implements ModInitializer {
    public static final String MOD_ID = "totemcounter";
    private static final Map<UUID, Integer> LAST_COUNT = new HashMap<>();

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(TotemSyncPayload.ID, TotemSyncPayload.CODEC);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 10 != 0) return;

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                int currentTotems = countTotems(player);
                UUID uuid = player.getUuid();

                if (LAST_COUNT.getOrDefault(uuid, -1) != currentTotems) {
                    LAST_COUNT.put(uuid, currentTotems);
                    TotemSyncPayload payload = new TotemSyncPayload(player.getId(), currentTotems);

                    for (ServerPlayerEntity tracker : PlayerLookup.tracking(player)) {
                        ServerPlayNetworking.send(tracker, payload);
                    }
                    ServerPlayNetworking.send(player, payload);
                }
            }
        });
    }

    public static int countTotems(ServerPlayerEntity player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().main) {
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                count += stack.getCount();
            }
        }
        for (ItemStack stack : player.getInventory().offHand) {
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                count += stack.getCount();
            }
        }
        return count;
    }
}
