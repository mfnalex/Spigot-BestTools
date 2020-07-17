package de.jeff_media.BestTools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class CommandReload {

    static void reload(CommandSender sender, Command command, Main main) {


            if (!sender.hasPermission("besttools.reload")) {
                sender.sendMessage(Objects.requireNonNull(command.getPermissionMessage()));
                return;
            }
            main.load(true);
            sender.sendMessage(ChatColor.GREEN + main.getName() + " has been reloaded.");
    }

}
