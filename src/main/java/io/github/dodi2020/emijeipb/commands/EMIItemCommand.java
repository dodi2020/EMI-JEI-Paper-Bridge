package io.github.dodi2020.emijeipb.commands;

import io.github.dodi2020.emijeipb.EMIJEIPaperBridge;
import io.github.dodi2020.emijeipb.managers.ItemGiveManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command to give items via EMI/JEI interface
 * Usage: /emiitem <player> <item> [amount] [nbt]
 */
public class EMIItemCommand implements CommandExecutor, TabCompleter {
    
    private final EMIJEIPaperBridge plugin;
    private final ItemGiveManager itemGiveManager;
    
    public EMIItemCommand(EMIJEIPaperBridge plugin) {
        this.plugin = plugin;
        this.itemGiveManager = plugin.getItemGiveManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender has permission
        if (!sender.hasPermission("emijeipb.give")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }
        
        // Handle self-give (no player specified)
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /" + label + " <player> <item> [amount] [nbt]");
            sender.sendMessage("§cOr: /" + label + " <item> [amount] [nbt] (to give to yourself)");
            return true;
        }
        
        Player targetPlayer;
        String itemId;
        int amount = 1;
        String nbt = null;
        int argOffset = 0;
        
        // Check if first argument is a player name
        Player potentialPlayer = Bukkit.getPlayer(args[0]);
        if (potentialPlayer != null && args.length > 1) {
            // First arg is a player, giving to another player
            if (!sender.hasPermission("emijeipb.give.others")) {
                sender.sendMessage("§cYou don't have permission to give items to other players!");
                return true;
            }
            targetPlayer = potentialPlayer;
            argOffset = 1;
        } else {
            // First arg is not a player or no second arg, giving to self
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cYou must specify a player when using this command from console!");
                return true;
            }
            targetPlayer = (Player) sender;
            argOffset = 0;
        }
        
        // Parse item ID
        if (args.length <= argOffset) {
            sender.sendMessage("§cYou must specify an item ID!");
            return true;
        }
        itemId = args[argOffset];
        
        // Parse amount if provided
        if (args.length > argOffset + 1) {
            try {
                amount = Integer.parseInt(args[argOffset + 1]);
                if (amount <= 0) {
                    sender.sendMessage("§cAmount must be positive!");
                    return true;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid amount: " + args[argOffset + 1]);
                return true;
            }
        }
        
        // Parse NBT if provided
        if (args.length > argOffset + 2) {
            StringBuilder nbtBuilder = new StringBuilder();
            for (int i = argOffset + 2; i < args.length; i++) {
                if (i > argOffset + 2) nbtBuilder.append(" ");
                nbtBuilder.append(args[i]);
            }
            nbt = nbtBuilder.toString();
        }
        
        // Give the item
        boolean success = itemGiveManager.giveItem(targetPlayer, itemId, amount, nbt, 
            io.github.dodi2020.emijeipb.events.EMIJEIGiveItemEvent.Source.COMMAND);
        
        if (success) {
            if (targetPlayer.equals(sender)) {
                sender.sendMessage("§aGave you " + amount + " x " + itemId);
            } else {
                sender.sendMessage("§aGave " + targetPlayer.getName() + " " + amount + " x " + itemId);
                targetPlayer.sendMessage("§aYou received " + amount + " x " + itemId);
            }
        } else {
            sender.sendMessage("§cFailed to give item. Check console for errors.");
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!sender.hasPermission("emijeipb.give")) {
            return completions;
        }
        
        if (args.length == 1) {
            // First argument: player name or item
            // Add player names
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
            // Add common items
            addCommonItems(completions, args[0]);
        } else if (args.length == 2) {
            // Second argument: item ID (if first was a player) or amount
            Player potentialPlayer = Bukkit.getPlayer(args[0]);
            if (potentialPlayer != null) {
                // First arg was a player, suggest items
                addCommonItems(completions, args[1]);
            } else {
                // First arg was an item, suggest amounts
                completions.addAll(Arrays.asList("1", "16", "32", "64"));
            }
        } else if (args.length == 3) {
            // Third argument: amount
            Player potentialPlayer = Bukkit.getPlayer(args[0]);
            if (potentialPlayer != null) {
                // Suggest amounts
                completions.addAll(Arrays.asList("1", "16", "32", "64"));
            }
        }
        
        return completions;
    }
    
    /**
     * Add common Minecraft items to the completion list
     */
    private void addCommonItems(List<String> completions, String partial) {
        String[] commonItems = {
            "minecraft:diamond", "minecraft:iron_ingot", "minecraft:gold_ingot",
            "minecraft:emerald", "minecraft:netherite_ingot", "minecraft:stick",
            "minecraft:stone", "minecraft:cobblestone", "minecraft:dirt",
            "minecraft:oak_planks", "minecraft:oak_log", "minecraft:coal",
            "minecraft:bread", "minecraft:apple", "minecraft:golden_apple",
            "minecraft:iron_sword", "minecraft:iron_pickaxe", "minecraft:iron_axe",
            "minecraft:diamond_sword", "minecraft:diamond_pickaxe", "minecraft:bow",
            "minecraft:arrow", "minecraft:ender_pearl", "minecraft:command_block"
        };
        
        String lowerPartial = partial.toLowerCase();
        for (String item : commonItems) {
            if (item.toLowerCase().contains(lowerPartial)) {
                completions.add(item);
            }
        }
    }
}
