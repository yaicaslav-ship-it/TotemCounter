package com.example.totemcounter.client;

import com.example.totemcounter.network.TotemSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class TotemCounterClient implements ClientModInitializer {
    public static final Map<Integer, Integer> TOTEM_COUNTS = new ConcurrentHashMap<>();

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(TotemSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                TOTEM_COUNTS.put(payload.entityId(), payload.totemCount());
            });
        });
    }
}
