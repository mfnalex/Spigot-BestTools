package de.jeff_media.BestTool;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandRefill implements CommandExecutor {

    Main main;

    CommandRefill(Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player p;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            CommandReload.reload(sender,command,main);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        p = (Player) sender;

        // Toggle AutoRefill //
        if(main.getPlayerSetting(p).toggleRefillEnabled()) {
            p.sendMessage(main.messages.MSG_REFILL_ENABLED);
        } else {
            p.sendMessage(main.messages.MSG_REFILL_DISABLED);
        }

        return true;
    }
}
