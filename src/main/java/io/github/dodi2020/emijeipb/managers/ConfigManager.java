package io.github.dodi2020.emijeipb.managers;

import io.github.dodi2020.emijeipb.EMIJEIPaperBridge;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages plugin configuration
 */
public class ConfigManager {
    
    private final EMIJEIPaperBridge plugin;
    private FileConfiguration config;
    
    private boolean debug;
    private boolean allowCheatMode;
    private int maxCheatStackSize;
    private boolean allowCustomItems;
    private boolean enablePluginChannels;
    private String emiChannel;
    private String jeiChannel;
    private boolean logItemGives;
    private boolean logProtocolMessages;
    private List<String> blacklistedItems;
    private Map<String, String> restrictedItems;
    
    public ConfigManager(EMIJEIPaperBridge plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Load configuration from config.yml
     */
    public void loadConfig() {
        config = plugin.getConfig();
        
        // Load settings
        debug = config.getBoolean("debug", false);
        allowCheatMode = config.getBoolean("allow-cheat-mode", true);
        maxCheatStackSize = config.getInt("max-cheat-stack-size", 64);
        allowCustomItems = config.getBoolean("allow-custom-items", true);
        
        // Protocol settings
        enablePluginChannels = config.getBoolean("protocol.enable-plugin-channels", true);
        emiChannel = config.getString("protocol.channels.emi", "emi:main");
        jeiChannel = config.getString("protocol.channels.jei", "jei:main");
        
        // Logging settings
        logItemGives = config.getBoolean("logging.log-item-gives", true);
        logProtocolMessages = config.getBoolean("logging.log-protocol-messages", false);
        
        // Load restrictions
        blacklistedItems = config.getStringList("restrictions.blacklisted-items");
        if (blacklistedItems == null) {
            blacklistedItems = new ArrayList<>();
        }
        
        restrictedItems = new HashMap<>();
        if (config.getConfigurationSection("restrictions.restricted-items") != null) {
            for (String key : config.getConfigurationSection("restrictions.restricted-items").getKeys(false)) {
                String permission = config.getString("restrictions.restricted-items." + key);
                restrictedItems.put(key, permission);
            }
        }
        
        if (debug) {
            plugin.getLogger().info("Configuration loaded with debug mode enabled");
        }
    }
    
    // Getters
    public boolean isDebug() {
        return debug;
    }
    
    public boolean isAllowCheatMode() {
        return allowCheatMode;
    }
    
    public int getMaxCheatStackSize() {
        return maxCheatStackSize;
    }
    
    public boolean isAllowCustomItems() {
        return allowCustomItems;
    }
    
    public boolean isPluginChannelsEnabled() {
        return enablePluginChannels;
    }
    
    public String getEMIChannel() {
        return emiChannel;
    }
    
    public String getJEIChannel() {
        return jeiChannel;
    }
    
    public boolean isLogItemGives() {
        return logItemGives;
    }
    
    public boolean isLogProtocolMessages() {
        return logProtocolMessages;
    }
    
    public List<String> getBlacklistedItems() {
        return blacklistedItems;
    }
    
    public Map<String, String> getRestrictedItems() {
        return restrictedItems;
    }
}
