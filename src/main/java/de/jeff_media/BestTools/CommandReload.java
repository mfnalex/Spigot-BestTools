package de.jeff_media.BestTools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandReload {

    static void reload(CommandSender sender, Command command, Main main) {


            if (!sender.hasPermission("besttools.reload")) {
                sender.sendMessage(ChatColor.YELLOW + main.getName() + ": you don't have permission to use this command.");
                return;
            }
            main.load(true);
            sender.sendMessage(ChatColor.GREEN + main.getName() + " has been reloaded.");
    }

}
