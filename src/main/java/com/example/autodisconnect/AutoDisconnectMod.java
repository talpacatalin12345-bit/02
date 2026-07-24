package com.example.autodisconnect;

import com.example.autodisconnect.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.LiteralText;

public class AutoDisconnectMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null || client.player == null) return;
            
            ModConfig config = ModConfig.INSTANCE;
            if (!config.enabled) return;

            // 1. Проверка по 3D-миру (по координатам)
            for (AbstractClientPlayerEntity player : client.world.getPlayers()) {
                if (player == client.player) continue;

                String name = player.getGameProfile().getName();
                if (config.whitelist.contains(name)) continue;

                double distance = client.player.distanceTo(player);
                if (distance <= config.radius) {
                    disconnect(client, "Игрок рядом: " + name + " [" + (int) distance + " б.]");
                    return;
                }
            }

            // 2. Проверка по Tab-листу (если включена опция)
            if (config.checkTabList && client.getNetworkHandler() != null) {
                for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
                    String name = entry.getProfile().getName();
                    if (name.equals(client.player.getGameProfile().getName())) continue;
                    if (config.whitelist.contains(name)) continue;

                    disconnect(client, "Игрок обнаружен в Tab-листе: " + name);
                    return;
                }
            }
        });
    }

    private void disconnect(MinecraftClient client, String reason) {
        if (client.getNetworkHandler() != null) {
            client.getNetworkHandler().getConnection().disconnect(
                new LiteralText("§c[AutoDisconnect]\n§f" + reason)
            );
        }
    }
}
