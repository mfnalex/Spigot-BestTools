package de.jeff_media.BestTools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandRefill implements CommandExecutor, TabCompleter {

    Main main;

    CommandRefill(Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player p;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("besttool.reload")) {
                sender.sendMessage(command.getPermissionMessage());
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

        // Toggle AutoRefill //
        if(main.getPlayerSetting(p).toggleRefillEnabled()) {
            p.sendMessage(main.messages.MSG_REFILL_ENABLED);
        } else {
            p.sendMessage(main.messages.MSG_REFILL_DISABLED);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,  @NotNull String[] str) {
        final String[] args = {"toggle","on","off","preventItemBreak"};
        final String[] trueFalse = {"true","false"};


        return null;
    }
}
