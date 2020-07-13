package de.jeff_media.BestTool;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandReload {

    static void reload(CommandSender sender, Command command, Main main) {


            if (!sender.hasPermission("besttool.reload")) {
                sender.sendMessage(command.getPermissionMessage());
                return;
            }
            main.load(true);
            sender.sendMessage(ChatColor.GREEN + main.getName() + " has been reloaded.");
            return;
    }

}
