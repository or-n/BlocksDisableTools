package com.example.BlocksDisableTools;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class BlocksDisableTools extends JavaPlugin implements Listener {

    private Map<Player, Boolean> playerHasBlocks = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("BlocksDisableTools Plugin enabled!");
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    int blocks = countBlocks(player.getInventory());
                    updateBlockStatus(player, blocks > 0);
                }
            }
        }.runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        getLogger().info("BlocksDisableTools Plugin disabled!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        var inventory = player.getInventory();
        if (countBlocks(inventory) > 0 &&
            isTool(inventory.getItemInMainHand().getType())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null &&
                isUsable(event.getClickedBlock().getType())
            ) {
                return;
            }
        }
        Player player = event.getPlayer();
        var inventory = player.getInventory();
        if (inventory.getItemInOffHand().getType() == Material.SHIELD) {
            return;
        }
        if (countBlocks(inventory) > 0 &&
            isTool(inventory.getItemInMainHand().getType())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            var inventory = player.getInventory();
            if (countBlocks(inventory) > 0 &&
                isTool(inventory.getItemInMainHand().getType())
            ) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isTool(Material material) {
        switch (material) {
            case WOODEN_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLDEN_PICKAXE:
            case DIAMOND_PICKAXE:
            case NETHERITE_PICKAXE:
                return true;

            case WOODEN_AXE:
            case STONE_AXE:
            case IRON_AXE:
            case GOLDEN_AXE:
            case DIAMOND_AXE:
            case NETHERITE_AXE:
                return true;

            case WOODEN_SHOVEL:
            case STONE_SHOVEL:
            case IRON_SHOVEL:
            case GOLDEN_SHOVEL:
            case DIAMOND_SHOVEL:
            case NETHERITE_SHOVEL:
                return true;

            case WOODEN_HOE:
            case STONE_HOE:
            case IRON_HOE:
            case GOLDEN_HOE:
            case DIAMOND_HOE:
            case NETHERITE_HOE:
                return true;

            case WOODEN_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLDEN_SWORD:
            case DIAMOND_SWORD:
            case NETHERITE_SWORD:
                return true;

            case TRIDENT:
                return true;

            case BOW:
            case CROSSBOW:
                return true;

            case SHEARS:
                return true;
    
            default:
                return false;
        }
    }

    private boolean isUsable(Material material) {
        switch (material) {
            case CHEST:
            case TRAPPED_CHEST:
            case FURNACE:
            case BLAST_FURNACE:
            case SMOKER:
            case HOPPER:
            case DISPENSER:
            case DROPPER:
            case BARREL:
            case SHULKER_BOX:
                return true;
            case CRAFTING_TABLE:
                return true;
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case ACACIA_DOOR:
            case DARK_OAK_DOOR:
            case MANGROVE_DOOR:
            case CHERRY_DOOR:
            case BAMBOO_DOOR:
            case CRIMSON_DOOR:
            case WARPED_DOOR:
            case IRON_DOOR:
                return true;
            default:
                return false;
        }
    }

    private int countBlocks(PlayerInventory inventory) {
        int blockCount = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType().isBlock()) {
                blockCount += item.getAmount();
            }
        }
        return blockCount;
    }

    private void updateBlockStatus(Player player, boolean hasBlocksNow) {
        var hadBlocksBefore = playerHasBlocks.getOrDefault(player, false);
        if (!hadBlocksBefore && hasBlocksNow) {
            player.sendMessage("Podniosłeś bloki!");
        }
        if (hadBlocksBefore && !hasBlocksNow) {
            player.sendMessage("Już nie masz bloków!");
        }
        playerHasBlocks.put(player, hasBlocksNow);
    }
}