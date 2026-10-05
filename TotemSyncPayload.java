package com.example.totemcounter.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TotemSyncPayload(int entityId, int totemCount) implements CustomPayload {
    public static final CustomPayload.Id<TotemSyncPayload> ID = 
        new CustomPayload.Id<>(Identifier.of("totemcounter", "sync_totems"));

    public static final PacketCodec<RegistryByteBuf, TotemSyncPayload> CODEC = 
        PacketCodec.tuple(
            PacketCodecs.VAR_INT, TotemSyncPayload::entityId,
            PacketCodecs.VAR_INT, TotemSyncPayload::totemCount,
            TotemSyncPayload::new
        );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
