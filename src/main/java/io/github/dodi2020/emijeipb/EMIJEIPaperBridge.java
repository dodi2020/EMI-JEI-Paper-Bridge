package io.github.dodi2020.emijeipb;

import io.github.dodi2020.emijeipb.commands.EMIItemCommand;
import io.github.dodi2020.emijeipb.commands.EMIReloadCommand;
import io.github.dodi2020.emijeipb.listeners.PluginMessageListener;
import io.github.dodi2020.emijeipb.managers.ConfigManager;
import io.github.dodi2020.emijeipb.managers.ItemGiveManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Main plugin class for EMI-JEI-Paper-Bridge
 * Bridges EMI/JEI client mods with Paper server functionality
 */
public class EMIJEIPaperBridge extends JavaPlugin {
    
    private static EMIJEIPaperBridge instance;
    private ConfigManager configManager;
    private ItemGiveManager itemGiveManager;
    private PluginMessageListener pluginMessageListener;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        configManager = new ConfigManager(this);
        itemGiveManager = new ItemGiveManager(this);
        
        // Load configuration
        saveDefaultConfig();
        configManager.loadConfig();
        
        // Register commands
        registerCommands();
        
        // Register listeners
        registerListeners();
        
        // Register plugin messaging channels
        registerPluginChannels();
        
        getLogger().info("EMI-JEI-Paper-Bridge has been enabled!");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("Supporting EMI and JEI client mods for Minecraft 1.21.1");
    }
    
    @Override
    public void onDisable() {
        // Unregister plugin messaging channels
        unregisterPluginChannels();
        
        getLogger().info("EMI-JEI-Paper-Bridge has been disabled!");
    }
    
    /**
     * Register plugin commands
     */
    private void registerCommands() {
        getCommand("emiitem").setExecutor(new EMIItemCommand(this));
        getCommand("emireload").setExecutor(new EMIReloadCommand(this));
        getLogger().info("Registered commands: /emiitem, /emireload");
    }
    
    /**
     * Register event listeners
     */
    private void registerListeners() {
        pluginMessageListener = new PluginMessageListener(this);
        getServer().getPluginManager().registerEvents(pluginMessageListener, this);
        getLogger().info("Registered event listeners");
    }
    
    /**
     * Register plugin messaging channels for EMI/JEI communication
     */
    private void registerPluginChannels() {
        if (!configManager.isPluginChannelsEnabled()) {
            getLogger().info("Plugin messaging channels are disabled in config");
            return;
        }
        
        try {
            // Register EMI channel
            String emiChannel = configManager.getEMIChannel();
            getServer().getMessenger().registerIncomingPluginChannel(this, emiChannel, pluginMessageListener);
            getServer().getMessenger().registerOutgoingPluginChannel(this, emiChannel);
            getLogger().info("Registered EMI plugin channel: " + emiChannel);
            
            // Register JEI channel
            String jeiChannel = configManager.getJEIChannel();
            getServer().getMessenger().registerIncomingPluginChannel(this, jeiChannel, pluginMessageListener);
            getServer().getMessenger().registerOutgoingPluginChannel(this, jeiChannel);
            getLogger().info("Registered JEI plugin channel: " + jeiChannel);
            
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to register plugin messaging channels", e);
        }
    }
    
    /**
     * Unregister plugin messaging channels
     */
    private void unregisterPluginChannels() {
        if (!configManager.isPluginChannelsEnabled()) {
            return;
        }
        
        try {
            String emiChannel = configManager.getEMIChannel();
            String jeiChannel = configManager.getJEIChannel();
            
            getServer().getMessenger().unregisterIncomingPluginChannel(this, emiChannel);
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, emiChannel);
            
            getServer().getMessenger().unregisterIncomingPluginChannel(this, jeiChannel);
            getServer().getMessenger().unregisterOutgoingPluginChannel(this, jeiChannel);
            
            getLogger().info("Unregistered plugin messaging channels");
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "Error unregistering plugin messaging channels", e);
        }
    }
    
    /**
     * Reload plugin configuration
     */
    public void reloadConfiguration() {
        reloadConfig();
        configManager.loadConfig();
        getLogger().info("Configuration reloaded successfully");
    }
    
    // Getters
    public static EMIJEIPaperBridge getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public ItemGiveManager getItemGiveManager() {
        return itemGiveManager;
    }
}
