package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;

public class SpearMaceMod implements ClientModInitializer {
    
    private static boolean didHit = false;
    private static int ticks = 0;

    @Override
    public void onInitializeClient() {
        // Spear se hit detect karo
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient) return ActionResult.PASS;
            
            // Slot 1 = 0 me trident hai? 
            if (player.getInventory().selectedSlot == 0 && 
                player.getMainHandStack().isOf(Items.TRIDENT)) {
                didHit = true;
                ticks = 0;
            }
            return ActionResult.PASS;
        });

        // 3 tick baad mace pe swap
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!didHit || client.player == null) return;
            
            ticks++;
            
            if (ticks == 3) {
                client.player.getInventory().selectedSlot = 3; // Slot 4 = Mace
            }
            
            if (ticks >= 10) {
                didHit = false;
                ticks = 0;
            }
        });
    }
}
