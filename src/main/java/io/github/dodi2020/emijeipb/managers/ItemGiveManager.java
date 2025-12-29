package io.github.dodi2020.emijeipb.managers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.dodi2020.emijeipb.EMIJEIPaperBridge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.logging.Level;

/**
 * Manages item giving functionality for EMI/JEI
 */
public class ItemGiveManager {
    
    private final EMIJEIPaperBridge plugin;
    private final ConfigManager configManager;
    
    public ItemGiveManager(EMIJEIPaperBridge plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }
    
    /**
     * Give an item to a player
     * 
     * @param player The player to give the item to
     * @param itemId The Minecraft item ID (e.g., "minecraft:diamond")
     * @param amount The amount of items to give
     * @param nbt Optional NBT data for the item
     * @return true if the item was given successfully
     */
    public boolean giveItem(Player player, String itemId, int amount, String nbt) {
        if (!configManager.isAllowCheatMode()) {
            return false;
        }
        
        // Check if player has permission
        if (!player.hasPermission("emijeipb.cheat") && !player.hasPermission("emijeipb.give")) {
            player.sendMessage("§cYou don't have permission to use cheat mode!");
            return false;
        }
        
        // Check if item is blacklisted
        if (configManager.getBlacklistedItems().contains(itemId)) {
            player.sendMessage("§cThis item cannot be given via cheat mode!");
            return false;
        }
        
        // Check if item has restricted permission
        if (configManager.getRestrictedItems().containsKey(itemId)) {
            String requiredPermission = configManager.getRestrictedItems().get(itemId);
            if (!player.hasPermission(requiredPermission)) {
                player.sendMessage("§cYou don't have permission to give this item!");
                return false;
            }
        }
        
        // Validate stack size
        int maxStackSize = configManager.getMaxCheatStackSize();
        if (maxStackSize > 0 && amount > maxStackSize) {
            amount = maxStackSize;
        }
        
        try {
            // Parse the item ID to get Material
            Material material = parseMaterial(itemId);
            if (material == null || material == Material.AIR) {
                player.sendMessage("§cInvalid item: " + itemId);
                return false;
            }
            
            // Create the item stack
            ItemStack itemStack = new ItemStack(material, amount);
            
            // Apply NBT data if provided
            if (nbt != null && !nbt.isEmpty()) {
                applyNBT(itemStack, nbt);
            }
            
            // Give the item to the player
            player.getInventory().addItem(itemStack);
            
            // Log if enabled
            if (configManager.isLogItemGives()) {
                plugin.getLogger().info(String.format("Gave %d x %s to %s", amount, itemId, player.getName()));
            }
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to give item " + itemId + " to " + player.getName(), e);
            player.sendMessage("§cFailed to give item: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Parse a Minecraft item ID to a Material
     * Handles both namespaced IDs (minecraft:diamond) and simple names (diamond)
     */
    private Material parseMaterial(String itemId) {
        try {
            // Remove namespace if present
            String materialName = itemId;
            if (itemId.contains(":")) {
                String[] parts = itemId.split(":");
                materialName = parts[parts.length - 1];
            }
            
            // Convert to uppercase and replace hyphens with underscores
            materialName = materialName.toUpperCase().replace("-", "_");
            
            return Material.matchMaterial(materialName);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to parse material: " + itemId, e);
            return null;
        }
    }
    
    /**
     * Apply NBT data to an item stack
     * This is a simplified implementation - full NBT support would require NMS or additional libraries
     */
    private void applyNBT(ItemStack itemStack, String nbt) {
        // Basic NBT parsing - this is simplified
        // In a production environment, you'd want to use proper NBT parsing
        
        try {
            // For now, just try to parse basic name and lore from JSON-like NBT
            if (nbt.contains("display") && nbt.contains("Name")) {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    // Extract display name if present
                    // This is a very basic implementation
                    String displayName = extractNBTValue(nbt, "Name");
                    if (displayName != null) {
                        meta.setDisplayName(displayName);
                    }
                    itemStack.setItemMeta(meta);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to apply NBT: " + nbt, e);
        }
    }
    
    /**
     * Extract a value from NBT string
     * This is a very simplified implementation
     */
    private String extractNBTValue(String nbt, String key) {
        try {
            int keyIndex = nbt.indexOf(key);
            if (keyIndex == -1) return null;
            
            int colonIndex = nbt.indexOf(":", keyIndex);
            int startQuote = nbt.indexOf("\"", colonIndex);
            int endQuote = nbt.indexOf("\"", startQuote + 1);
            
            if (startQuote != -1 && endQuote != -1) {
                return nbt.substring(startQuote + 1, endQuote);
            }
        } catch (Exception e) {
            // Ignore parsing errors
        }
        return null;
    }
    
    /**
     * Handle a plugin message for item giving
     * This is used when EMI/JEI sends a request via plugin channels
     */
    public void handlePluginMessage(Player player, byte[] message) {
        try {
            ByteArrayDataInput input = ByteStreams.newDataInput(message);
            
            // Read the action type
            String action = input.readUTF();
            
            if (configManager.isLogProtocolMessages()) {
                plugin.getLogger().info("Received plugin message from " + player.getName() + ": " + action);
            }
            
            switch (action) {
                case "GiveItem":
                case "CheatItem":
                    handleGiveItemMessage(player, input);
                    break;
                case "RequestCheatPermission":
                    handleCheatPermissionRequest(player);
                    break;
                case "SetHotbarItem":
                    handleSetHotbarItem(player, input);
                    break;
                default:
                    if (configManager.isDebug()) {
                        plugin.getLogger().warning("Unknown plugin message action: " + action);
                    }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to handle plugin message from " + player.getName(), e);
        }
    }
    
    /**
     * Handle a give item message from the client
     */
    private void handleGiveItemMessage(Player player, ByteArrayDataInput input) {
        try {
            String itemId = input.readUTF();
            int amount = input.readInt();
            String nbt = null;
            
            // Try to read NBT if present
            try {
                nbt = input.readUTF();
            } catch (Exception e) {
                // No NBT data
            }
            
            giveItem(player, itemId, amount, nbt);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to parse give item message", e);
        }
    }
    
    /**
     * Handle a cheat permission request from the client
     */
    private void handleCheatPermissionRequest(Player player) {
        boolean hasPermission = player.hasPermission("emijeipb.cheat");
        
        if (configManager.isDebug()) {
            plugin.getLogger().info(player.getName() + " requested cheat permission: " + hasPermission);
        }
        
        // Send permission status back to client if needed
        // This would require implementing outgoing messages
    }
    
    /**
     * Handle setting an item in the hotbar via JEI
     */
    private void handleSetHotbarItem(Player player, ByteArrayDataInput input) {
        try {
            int slot = input.readInt();
            String itemId = input.readUTF();
            int amount = input.readInt();
            
            if (slot < 0 || slot > 8) {
                return; // Invalid hotbar slot
            }
            
            Material material = parseMaterial(itemId);
            if (material == null || material == Material.AIR) {
                return;
            }
            
            ItemStack itemStack = new ItemStack(material, amount);
            player.getInventory().setItem(slot, itemStack);
            
            if (configManager.isLogItemGives()) {
                plugin.getLogger().info(String.format("Set hotbar slot %d for %s to %d x %s", slot, player.getName(), amount, itemId));
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to set hotbar item", e);
        }
    }
}
