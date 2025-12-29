package io.github.dodi2020.emijeipb.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Event fired when a player attempts to give themselves an item via EMI/JEI
 * This event can be cancelled to prevent the item from being given
 */
public class EMIJEIGiveItemEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    
    private final Player player;
    private final String itemId;
    private int amount;
    private String nbt;
    private ItemStack resultItem;
    private final Source source;
    
    /**
     * Source of the give item request
     */
    public enum Source {
        EMI,        // Request from EMI mod
        JEI,        // Request from JEI mod
        COMMAND,    // Request from /emiitem command
        UNKNOWN     // Unknown source
    }
    
    public EMIJEIGiveItemEvent(Player player, String itemId, int amount, String nbt, Source source) {
        this.player = player;
        this.itemId = itemId;
        this.amount = amount;
        this.nbt = nbt;
        this.source = source;
    }
    
    /**
     * Get the player receiving the item
     */
    public Player getPlayer() {
        return player;
    }
    
    /**
     * Get the item ID (e.g., "minecraft:diamond")
     */
    public String getItemId() {
        return itemId;
    }
    
    /**
     * Get the amount of items to give
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * Set the amount of items to give
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    /**
     * Get the NBT data for the item (may be null)
     */
    public String getNbt() {
        return nbt;
    }
    
    /**
     * Set the NBT data for the item
     */
    public void setNbt(String nbt) {
        this.nbt = nbt;
    }
    
    /**
     * Get the resulting ItemStack that will be given
     * This may be null if the ItemStack hasn't been created yet
     */
    public ItemStack getResultItem() {
        return resultItem;
    }
    
    /**
     * Set the resulting ItemStack to give
     * If set, this ItemStack will be given instead of creating one from the item ID
     */
    public void setResultItem(ItemStack resultItem) {
        this.resultItem = resultItem;
    }
    
    /**
     * Get the source of the give item request
     */
    public Source getSource() {
        return source;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
