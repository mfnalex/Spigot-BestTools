package de.jeff_media.BestTools.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class BestToolsNotifyEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Block block;

    public BestToolsNotifyEvent(@NotNull Player who, Block block) {
        super(who);
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
