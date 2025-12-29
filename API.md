# API Documentation

This document describes the API for developers who want to integrate with or extend EMI-JEI-Paper-Bridge.

## Maven/Gradle Dependency

Add the plugin as a dependency to your project:

### Maven
```xml
<repository>
    <id>jitpack</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.dodi2020</groupId>
    <artifactId>EMI-JEI-Paper-Bridge</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```

### Gradle
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.dodi2020:EMI-JEI-Paper-Bridge:1.0.0'
}
```

## Getting the Plugin Instance

```java
EMIJEIPaperBridge plugin = (EMIJEIPaperBridge) Bukkit.getPluginManager().getPlugin("EMI-JEI-Paper-Bridge");
```

## Events API

### EMIJEIGiveItemEvent

Listen for when players receive items via EMI/JEI:

```java
@EventHandler
public void onItemGive(EMIJEIGiveItemEvent event) {
    Player player = event.getPlayer();
    String itemId = event.getItemId();
    int amount = event.getAmount();
    
    // Log the action
    getLogger().info(player.getName() + " received " + amount + " x " + itemId);
    
    // Modify the amount
    event.setAmount(amount * 2); // Give double items
    
    // Provide custom ItemStack
    ItemStack custom = new ItemStack(Material.DIAMOND_SWORD);
    ItemMeta meta = custom.getItemMeta();
    meta.setDisplayName("§bCustom Sword");
    custom.setItemMeta(meta);
    event.setResultItem(custom);
    
    // Or cancel the event
    if (itemId.contains("bedrock")) {
        event.setCancelled(true);
        player.sendMessage("§cBedrock cannot be given!");
    }
}
```

### Event Properties

- `Player getPlayer()` - The player receiving the item
- `String getItemId()` - The Minecraft item ID (e.g., "minecraft:diamond")
- `int getAmount()` / `void setAmount(int)` - The amount of items
- `String getNbt()` / `void setNbt(String)` - NBT data for the item
- `ItemStack getResultItem()` / `void setResultItem(ItemStack)` - The actual ItemStack to give
- `Source getSource()` - Where the request came from (EMI, JEI, COMMAND, UNKNOWN)
- `boolean isCancelled()` / `void setCancelled(boolean)` - Cancel the item giving

### Event Source Types

```java
EMIJEIGiveItemEvent.Source.EMI      // From EMI mod
EMIJEIGiveItemEvent.Source.JEI      // From JEI mod
EMIJEIGiveItemEvent.Source.COMMAND  // From /emiitem command
EMIJEIGiveItemEvent.Source.UNKNOWN  // Unknown source
```

## Managers API

### ItemGiveManager

Access the item giving manager:

```java
ItemGiveManager manager = plugin.getItemGiveManager();

// Give an item programmatically
boolean success = manager.giveItem(
    player,                                    // Player
    "minecraft:diamond",                       // Item ID
    64,                                        // Amount
    null,                                      // NBT (optional)
    EMIJEIGiveItemEvent.Source.UNKNOWN        // Source
);
```

### ConfigManager

Access configuration values:

```java
ConfigManager config = plugin.getConfigManager();

// Check if cheat mode is allowed
if (config.isAllowCheatMode()) {
    // ...
}

// Get blacklisted items
List<String> blacklist = config.getBlacklistedItems();

// Get maximum stack size
int maxStack = config.getMaxCheatStackSize();

// Check debug mode
if (config.isDebug()) {
    // Log debug info
}
```

## Plugin Messaging Protocol

### Sending Messages to Clients

```java
// Send a message to a client
ByteArrayDataOutput out = ByteStreams.newDataOutput();
out.writeUTF("CustomAction");
out.writeInt(someValue);

String channel = plugin.getConfigManager().getEMIChannel(); // or getJEIChannel()
player.sendPluginMessage(plugin, channel, out.toByteArray());
```

### Custom Message Actions

The plugin handles these message actions:
- `GiveItem` - Give an item to the player
- `CheatItem` - Alternative item giving action
- `RequestCheatPermission` - Request cheat mode permission status
- `SetHotbarItem` - Set an item in player's hotbar

## Example Integrations

### Custom Item Validator

```java
@EventHandler
public void validateCustomItems(EMIJEIGiveItemEvent event) {
    String itemId = event.getItemId();
    
    // Check if item is from a specific mod
    if (itemId.startsWith("mymod:")) {
        // Validate the player has permission for mod items
        if (!event.getPlayer().hasPermission("mymod.items.receive")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou need permission to receive modded items!");
        }
    }
}
```

### Item Logging System

```java
@EventHandler
public void logItemGiving(EMIJEIGiveItemEvent event) {
    if (event.isCancelled()) return;
    
    // Log to database or file
    String log = String.format("[%s] %s received %d x %s from %s",
        LocalDateTime.now(),
        event.getPlayer().getName(),
        event.getAmount(),
        event.getItemId(),
        event.getSource()
    );
    
    // Save to your logging system
    myLogger.log(log);
}
```

### Economy Integration

```java
@EventHandler
public void chargeForItems(EMIJEIGiveItemEvent event) {
    Player player = event.getPlayer();
    int cost = calculateCost(event.getItemId(), event.getAmount());
    
    if (!economyAPI.hasMoney(player, cost)) {
        event.setCancelled(true);
        player.sendMessage("§cYou need $" + cost + " to get this item!");
        return;
    }
    
    economyAPI.withdrawMoney(player, cost);
    player.sendMessage("§aPaid $" + cost + " for items");
}
```

### Creative-Only Restriction

```java
@EventHandler
public void restrictToCreative(EMIJEIGiveItemEvent event) {
    Player player = event.getPlayer();
    
    if (player.getGameMode() != GameMode.CREATIVE) {
        event.setCancelled(true);
        player.sendMessage("§cYou must be in creative mode to use this!");
    }
}
```

## Plugin Dependencies

### Required
- Paper API 1.21.1 or higher

### Optional
- ProtocolLib - For advanced packet handling (if needed)
- Vault - For economy integration (if implementing custom handlers)

## Best Practices

1. **Always check if event is cancelled** before processing in low-priority listeners
2. **Use event priority** to control order of processing
3. **Handle null values** from NBT and ResultItem
4. **Validate item IDs** before processing
5. **Log important actions** for debugging and auditing

## Support

For API questions and issues:
- GitHub Issues: https://github.com/dodi2020/EMI-JEI-Paper-Bridge/issues
- API Examples: https://github.com/dodi2020/EMI-JEI-Paper-Bridge/wiki/API-Examples
