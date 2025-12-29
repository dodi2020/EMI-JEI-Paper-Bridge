package io.github.dodi2020.emijeipb.util;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Rate limiter to prevent spam and abuse
 */
public class RateLimiter {
    
    private final Map<UUID, Long> lastActionTime;
    private final Map<UUID, Integer> actionCount;
    private final long cooldownMs;
    private final int maxActions;
    
    /**
     * Create a rate limiter
     * 
     * @param cooldownMs Cooldown period in milliseconds
     * @param maxActions Maximum actions per cooldown period
     */
    public RateLimiter(long cooldownMs, int maxActions) {
        this.lastActionTime = new HashMap<>();
        this.actionCount = new HashMap<>();
        this.cooldownMs = cooldownMs;
        this.maxActions = maxActions;
    }
    
    /**
     * Check if a player is rate limited
     * 
     * @param player The player to check
     * @return true if the player can perform the action, false if rate limited
     */
    public boolean allowAction(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check if player has a last action time
        if (!lastActionTime.containsKey(playerId)) {
            // First action, allow it
            lastActionTime.put(playerId, currentTime);
            actionCount.put(playerId, 1);
            return true;
        }
        
        long lastTime = lastActionTime.get(playerId);
        long timeSinceLastAction = currentTime - lastTime;
        
        // If cooldown period has passed, reset counter
        if (timeSinceLastAction >= cooldownMs) {
            lastActionTime.put(playerId, currentTime);
            actionCount.put(playerId, 1);
            return true;
        }
        
        // Check if player has exceeded max actions
        int count = actionCount.getOrDefault(playerId, 0);
        if (count >= maxActions) {
            // Rate limited
            return false;
        }
        
        // Increment action count
        actionCount.put(playerId, count + 1);
        return true;
    }
    
    /**
     * Get remaining cooldown time in seconds
     * 
     * @param player The player to check
     * @return Remaining cooldown in seconds, or 0 if not rate limited
     */
    public int getRemainingCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (!lastActionTime.containsKey(playerId)) {
            return 0;
        }
        
        long lastTime = lastActionTime.get(playerId);
        long timeSinceLastAction = System.currentTimeMillis() - lastTime;
        
        if (timeSinceLastAction >= cooldownMs) {
            return 0;
        }
        
        int count = actionCount.getOrDefault(playerId, 0);
        if (count < maxActions) {
            return 0;
        }
        
        long remainingMs = cooldownMs - timeSinceLastAction;
        return (int) Math.ceil(remainingMs / 1000.0);
    }
    
    /**
     * Reset rate limit for a player
     * 
     * @param player The player to reset
     */
    public void reset(Player player) {
        UUID playerId = player.getUniqueId();
        lastActionTime.remove(playerId);
        actionCount.remove(playerId);
    }
    
    /**
     * Clear all rate limit data
     */
    public void clearAll() {
        lastActionTime.clear();
        actionCount.clear();
    }
}
