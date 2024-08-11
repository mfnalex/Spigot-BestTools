package de.jeff_media.BestTools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandRefill implements CommandExecutor, TabCompleter {

    final Main main;

    CommandRefill(Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player p;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("besttools.reload")) {
                sender.sendMessage(ChatColor.YELLOW + main.getName() + ": you don't have permission to use this command.");
                return true;
            }
            CommandReload.reload(sender,command,main);
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        p = (Player) sender;
        PlayerSetting playerSetting = main.getPlayerSetting(p);

        playerSetting.setHasSeenRefillMessage(true);

        // Toggle AutoRefill //
        if(playerSetting.toggleRefillEnabled()) {
            Messages.sendMessage(p,main.messages.MSG_REFILL_ENABLED);
        } else {
            Messages.sendMessage(p,main.messages.MSG_REFILL_DISABLED);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,  @NotNull String[] str) {
        // final String[] args = {"toggle","on","off","preventItemBreak","hotbar","hotbarOnly"};
        // final String[] trueFalse = {"true","false"};


        return null;
    }
}
