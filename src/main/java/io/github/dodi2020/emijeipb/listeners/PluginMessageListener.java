package io.github.dodi2020.emijeipb.listeners;

import io.github.dodi2020.emijeipb.EMIJEIPaperBridge;
import io.github.dodi2020.emijeipb.managers.ItemGiveManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.logging.Level;

/**
 * Listens for plugin messages from EMI/JEI client mods
 * Implements both Bukkit Listener and PluginMessageListener
 */
public class PluginMessageListener implements Listener, PluginMessageListener {
    
    private final EMIJEIPaperBridge plugin;
    private final ItemGiveManager itemGiveManager;
    
    public PluginMessageListener(EMIJEIPaperBridge plugin) {
        this.plugin = plugin;
        this.itemGiveManager = plugin.getItemGiveManager();
    }
    
    /**
     * Called when a plugin message is received from a client
     * This handles messages from both EMI and JEI mods
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (plugin.getConfigManager().isLogProtocolMessages()) {
            plugin.getLogger().info(String.format("Received plugin message on channel %s from %s (length: %d bytes)", 
                channel, player.getName(), message.length));
        }
        
        try {
            // Determine which mod sent the message based on the channel
            if (channel.equals(plugin.getConfigManager().getEMIChannel())) {
                handleEMIMessage(player, message);
            } else if (channel.equals(plugin.getConfigManager().getJEIChannel())) {
                handleJEIMessage(player, message);
            } else {
                if (plugin.getConfigManager().isDebug()) {
                    plugin.getLogger().warning("Received message on unknown channel: " + channel);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, 
                "Error handling plugin message from " + player.getName() + " on channel " + channel, e);
        }
    }
    
    /**
     * Handle a message from the EMI mod
     * EMI primarily uses vanilla commands, but this provides fallback support
     */
    private void handleEMIMessage(Player player, byte[] message) {
        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("Processing EMI message from " + player.getName());
        }
        
        // EMI typically uses vanilla /give commands, but we can handle
        // custom protocol messages if EMI sends them
        itemGiveManager.handlePluginMessage(player, message);
    }
    
    /**
     * Handle a message from the JEI mod
     * JEI has a more established plugin messaging protocol
     */
    private void handleJEIMessage(Player player, byte[] message) {
        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("Processing JEI message from " + player.getName());
        }
        
        // Process the JEI protocol message
        itemGiveManager.handlePluginMessage(player, message);
    }
    
    /**
     * Handle player join events
     * Send initial configuration to clients when they join
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("Player " + player.getName() + " joined, checking EMI/JEI permissions");
        }
        
        // Schedule a delayed task to send cheat permission status
        // This gives the client time to fully load
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            sendCheatPermissionStatus(player);
        }, 40L); // 2 seconds delay
    }
    
    /**
     * Send cheat permission status to the client
     * This informs the client whether the player can use cheat mode
     */
    private void sendCheatPermissionStatus(Player player) {
        if (!plugin.getConfigManager().isPluginChannelsEnabled()) {
            return;
        }
        
        boolean hasCheatPermission = player.hasPermission("emijeipb.cheat");
        
        if (plugin.getConfigManager().isDebug()) {
            plugin.getLogger().info("Sending cheat permission status to " + player.getName() + ": " + hasCheatPermission);
        }
        
        // Send permission status on both channels
        sendPermissionMessage(player, plugin.getConfigManager().getEMIChannel(), hasCheatPermission);
        sendPermissionMessage(player, plugin.getConfigManager().getJEIChannel(), hasCheatPermission);
    }
    
    /**
     * Send a permission message to the client
     */
    private void sendPermissionMessage(Player player, String channel, boolean hasPermission) {
        try {
            // Create a simple message indicating permission status
            byte[] message = new byte[] { (byte) (hasPermission ? 1 : 0) };
            player.sendPluginMessage(plugin, channel, message);
        } catch (Exception e) {
            if (plugin.getConfigManager().isDebug()) {
                plugin.getLogger().log(Level.WARNING, 
                    "Failed to send permission message to " + player.getName() + " on channel " + channel, e);
            }
        }
    }
}
