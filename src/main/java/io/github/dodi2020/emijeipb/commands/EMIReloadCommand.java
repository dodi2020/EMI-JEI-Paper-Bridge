package io.github.dodi2020.emijeipb.commands;

import io.github.dodi2020.emijeipb.EMIJEIPaperBridge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Command to reload the plugin configuration
 * Usage: /emireload
 */
public class EMIReloadCommand implements CommandExecutor {
    
    private final EMIJEIPaperBridge plugin;
    
    public EMIReloadCommand(EMIJEIPaperBridge plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has permission
        if (!sender.hasPermission("emijeipb.reload")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        try {
            // Reload the configuration
            plugin.reloadConfiguration();
            sender.sendMessage("§aEMI-JEI-Paper-Bridge configuration reloaded successfully!");
            sender.sendMessage("§7Check console for any configuration errors.");
        } catch (Exception e) {
            sender.sendMessage("§cFailed to reload configuration: " + e.getMessage());
            plugin.getLogger().warning("Error reloading configuration: " + e.getMessage());
            e.printStackTrace();
        }
        
        return true;
    }
}
